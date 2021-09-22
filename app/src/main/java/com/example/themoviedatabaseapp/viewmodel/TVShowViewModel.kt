package com.example.themoviedatabaseapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themoviedatabaseapp.model.current.CurResult
import com.example.themoviedatabaseapp.model.today.TdResult
import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails
import com.example.themoviedatabaseapp.repository.TVRepo
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
        loadingState.value = LoadingState.LOADING
        viewModelScope.launch {
            var tvDetails: TVShowDetails? = null
            try {
                tvDetails = repo.getTVDetail(id)

            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is UnknownHostException -> errorMessage.value = "No Network!"
                    else -> errorMessage.value = e.localizedMessage
                }
                loadingState.value = LoadingState.ERROR
            }

            if (tvDetails == null) {
                errorMessage.value = "No Data Found"
                loadingState.value = LoadingState.ERROR
            } else {
                loadingState.value = LoadingState.SUCCESS
                showTVDetails.value = tvDetails
                addShowToDB(tvDetails)
                addTvShowToFbDb(tvDetails)
            }
        }
    }

    private fun addShowToDB(tvShow: TVShowDetails) {
        viewModelScope.launch {
            try {
                repo.addTVToDB(tvShow)
                Log.i("ViewModel", tvShow.name!!)
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
                Log.i("ViewModel-GetCount", count.toString())
                if (count > 0) {
                    getShowFromDB(id)
                } else {
                    //fetchTVDetails(id)
                    getTvShowFromFbDb(id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getShowFromDB(id: Int) {
        loadingState.value = LoadingState.LOADING
        viewModelScope.launch {
            var tvShowDetails: TVShowDetails? = null
            try {
                tvShowDetails = repo.getTVFromDB(id)
            } catch (e: Exception) {
                e.printStackTrace()
                loadingState.value = LoadingState.ERROR
            }
            when {
                tvShowDetails != null -> {
                    showTVDetails.value = tvShowDetails
                    loadingState.value = LoadingState.SUCCESS
                }
                else -> {
                    errorMessage.value = "No Data Found In DB"
                    loadingState.value = LoadingState.ERROR
                }
            }
        }
    }

    private fun delShowFromDB(id: Int) {
        viewModelScope.launch {
            try {
                repo.delTVFromDB(id)
                dbDelSuccess?.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                dbDelSuccess?.value = false
            }
        }
    }

    // Write data to Firebase database
    private fun addTvShowToFbDb(tvShow: TVShowDetails) {

        //Writing data into database using setValue() method
        mFirebaseDatabase!!.child(tvShow.id.toString()).setValue(tvShow)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("ViewModelDB", tvShow.name!!)
                    dBAddSuccess?.value = true
                } else {
                    dBAddSuccess?.value = false
                }
            }
    }

    // Read data from Firebase database
    private fun getTvShowFromFbDb(id: Int) {
        mFirebaseDatabase!!.child(id.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tvShow: TVShowDetails? = snapshot.getValue(TVShowDetails::class.java)
                    if (tvShow != null) {
                        showTVDetails.value = tvShow
                        loadingState.value = LoadingState.SUCCESS
                    } else {
                        fetchTVDetails(id)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    errorMessage.value = "No Data Found In DB"
                    loadingState.value = LoadingState.ERROR
                    //Failed to read value
                    Log.e(TAG, "Failed to read user", error.toException())

                }
            })
    }
    //Remove data from Firebase database
    private fun deleteShowFromFbDb(id: Int) {
        mFirebaseDatabase!!.child(id.toString()).removeValue()
            .addOnSuccessListener {
                dbDelSuccess?.value = true

            }.addOnFailureListener {
                it.printStackTrace()
                dbDelSuccess?.value = false
            }
    }

    fun deleteDetails(id: Int) {
        viewModelScope.launch {
            val count: Int
            try {
                count = repo.checkIfData(id)
                Log.i("ViewModel-GetCount-DB", count.toString())
                if (count > 0) {
                    delShowFromDB(id)
                } else {
                    Log.i("ViewModel-GetCount-DB", count.toString())
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