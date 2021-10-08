package com.example.themoviedatabaseapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themoviedatabaseapp.model.current.CurResult
import com.example.themoviedatabaseapp.model.today.TdResult
import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails
import com.example.themoviedatabaseapp.repository.TVRepo
import com.example.themoviedatabaseapp.utils.TVViewState
import com.google.firebase.database.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class TVShowViewModel(private val repo: TVRepo) : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()
    private val showTodayTVList: MutableLiveData<List<TdResult>> = MutableLiveData()
    private val showTVDetails: MutableLiveData<TVShowDetails> = MutableLiveData()
    private val showCurrentTVList: MutableLiveData<List<CurResult>> = MutableLiveData()
    private val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loadingState = MutableLiveData<LoadingState>()
    private val _viewState = MutableLiveData<TVViewState.ViewState>()
    val viewState: LiveData<TVViewState.ViewState>
        get() = _viewState

    var dBAddSuccess: MutableLiveData<Boolean>? = MutableLiveData()
    var dbDelSuccess: MutableLiveData<Boolean>? = MutableLiveData()

    private var mFirebaseDatabaseInstances: FirebaseDatabase? = null
    private var mFirebaseDatabase: DatabaseReference? = null

    //Creating member variables of FirebaseDatabase and DatabaseReference
    init {
        //Get instance of FirebaseDatabase
        mFirebaseDatabaseInstances = FirebaseDatabase.getInstance()
        //Getting reference to “tvShowDetails” node
        mFirebaseDatabase = mFirebaseDatabaseInstances!!.getReference("tvShowDetails")
    }

    fun tvCurrentFromViewModel() {
        loadingState.value = LoadingState.LOADING
        viewModelScope.launch {
            try {
                val curTVList = repo.getTVCurrent()
                if (curTVList.results.isEmpty()) {
                    errorMessage.value = "No Data Found"
                    loadingState.value = LoadingState.ERROR
                } else {
                    showCurrentTVList.value = curTVList.results
                    loadingState.value = LoadingState.SUCCESS
                }
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is UnknownHostException -> errorMessage.value = "No Network!"
                    else -> errorMessage.value = e.localizedMessage
                }
                loadingState.value = LoadingState.ERROR
            }
        }
    }

    fun tvTodayFromViewModel() {
        loadingState.value = LoadingState.LOADING
        viewModelScope.launch {
            try {
                val tdTVList = repo.getTVToday()
                if (tdTVList.results.isEmpty()) {
                    errorMessage.value = "No Data Found"
                    loadingState.value = LoadingState.ERROR
                } else {
                    showTodayTVList.value = tdTVList.results
                    loadingState.value = LoadingState.SUCCESS
                }
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is UnknownHostException -> errorMessage.value = "No Network!"
                    else -> errorMessage.value = e.localizedMessage
                }
                loadingState.value = LoadingState.ERROR
            }
        }
    }

    private fun fetchTVDetails(id: Int) {
        _viewState.value = TVViewState.ViewLoading
        viewModelScope.launch {
            var tvDetails: TVShowDetails? = null
            try {
                tvDetails = repo.getTVDetail(id)

            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is UnknownHostException -> _viewState.value = TVViewState.Error("No Network!")
                    else -> _viewState.value = TVViewState.Error(e.localizedMessage!!)
                }
            }

            if (tvDetails == null) {
                _viewState.value = TVViewState.Error("No Data Found")
            } else {
                _viewState.value = TVViewState.Success(tvDetails)
                Log.i("ViewModel-Details-From-Network", tvDetails.name.toString())
                addShowToDB(tvDetails)
                addTvShowToFbDb(tvDetails)
            }
        }
    }

    private fun addShowToDB(tvShow: TVShowDetails) {
        viewModelScope.launch {
            try {
                repo.addTVToDB(tvShow)
                Log.i("ViewModel-Added-to-DB", tvShow.name!!)
                dBAddSuccess?.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                dBAddSuccess?.value = false
            }
        }
    }

    fun getDetails(id: Int) {
        viewModelScope.launch {
            val count: Int
            try {
                count = repo.checkIfData(id)
                if (count > 0) {
                    getShowFromDB(id)
                    Log.i("ViewModel-GetCount-Details", count.toString())
                } else {
                    Log.i("ViewModel-GetCount-Details", count.toString())
                    getTvShowFromFbDb(id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getShowFromDB(id: Int) {
        _viewState.value = TVViewState.ViewLoading
        viewModelScope.launch {
            var tvShowDetails: TVShowDetails? = null
            try {
                tvShowDetails = repo.getTVFromDB(id)
            } catch (e: Exception) {
                e.printStackTrace()
                _viewState.value = TVViewState.Error(e.message.toString())
            }
            when {
                tvShowDetails != null -> {
                    _viewState.value = TVViewState.Success(tvShowDetails)
                    Log.i("ViewModel-Details-From-DB", tvShowDetails.name.toString())
                }
                else -> {
                    _viewState.value = TVViewState.Error("No Data Found In DB")
                }
            }
        }
    }

    private fun delShowFromDB(id: Int) {
        viewModelScope.launch {
            val numRow: Int
            try {
                numRow = repo.delTVFromDB(id)
                dbDelSuccess?.value = true
                Log.i("ViewModel-GetCount-Delete-DB", numRow.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                dbDelSuccess?.value = false
            }
        }
    }

    // Write data to Firebase database
    private fun addTvShowToFbDb(tvShow: TVShowDetails) {
        viewModelScope.launch {
            try {
                //Writing data into database using setValue() method
                mFirebaseDatabase!!.child(tvShow.id.toString()).setValue(tvShow)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i("ViewModel-Added-to-FbDB", tvShow.name!!)
                            dBAddSuccess?.value = true
                        } else {
                            dBAddSuccess?.value = false
                        }
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Read data from Firebase database
    private fun getTvShowFromFbDb(id: Int) {
        _viewState.value = TVViewState.ViewLoading
        viewModelScope.launch {
            try {
                mFirebaseDatabase!!.child(id.toString())
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val tvShow: TVShowDetails? =
                                snapshot.getValue(TVShowDetails::class.java)
                            if (tvShow != null) {
                                _viewState.value =
                                    TVViewState.Success(tvShow)
                            } else {
                                fetchTVDetails(id)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            _viewState.value = TVViewState.Error("No Data Found In FbDB")
                            //Failed to read value
                            Log.e(TAG, "Failed to read user", error.toException())
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //Remove data from Firebase database
    private fun deleteShowFromFbDb(id: Int) {
        viewModelScope.launch {
            try {
                mFirebaseDatabase!!.child(id.toString()).removeValue()
                    .addOnSuccessListener {
                        dbDelSuccess?.value = true
                        Log.i("ViewModel-GetCount-Delete-FbDB", id.toString())

                    }.addOnFailureListener {
                        it.printStackTrace()
                        dbDelSuccess?.value = false
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteDetails(id: Int) {
        viewModelScope.launch {
            val count: Int
            try {
                count = repo.checkIfData(id)
                if (count > 0) {
                    delShowFromDB(id)
                } else {
                    deleteShowFromFbDb(id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    enum class LoadingState {
        LOADING,
        SUCCESS,
        ERROR
    }

    fun curTVLiveData(): MutableLiveData<List<CurResult>> {
        return showCurrentTVList
    }

    fun todayTVLiveData(): MutableLiveData<List<TdResult>> {
        return showTodayTVList
    }

    fun errorMessage(): MutableLiveData<String> {
        return errorMessage
    }

    fun tvDetails(): MutableLiveData<TVShowDetails> {
        return showTVDetails
    }

    companion object {
        private const val TAG = "TVShowViewModel"
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}