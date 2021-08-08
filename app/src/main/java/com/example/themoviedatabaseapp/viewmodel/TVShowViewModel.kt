package com.example.themoviedatabaseapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themoviedatabaseapp.model.current.CurResult
import com.example.themoviedatabaseapp.model.today.TdResult
import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails
import com.example.themoviedatabaseapp.repository.TVRepo
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
            } catch (e: Exception){
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
            }catch (e: Exception){
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

            }catch (e: Exception){
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
            }
            else {
                loadingState.value = LoadingState.SUCCESS
                showTVDetails.value = tvDetails
                addShowToDB(tvDetails)
            }
        }
    }

    private fun addShowToDB(tvShow: TVShowDetails) {
        viewModelScope.launch {
            try {
                repo.addTVToDB(tvShow)
                Log.i("ViewModel", tvShow.name)
                dBAddSuccess?.value = true
            }catch (e: Exception){
                e.printStackTrace()
                dBAddSuccess?.value = false
            }
        }
    }

    fun getCount(id: Int){
        viewModelScope.launch {
            val count: Int
            try {
                count = repo.checkIfData(id)
                Log.i("ViewModel", count.toString())
                if (count > 0){
                    getShowFromDB(id)
                }
                else
                    fetchTVDetails(id)
            }catch (e: Exception){
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
            }catch (e: Exception){
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

    fun delShowFromDB(id: Int) {
        viewModelScope.launch {
           try {
               repo.delTVFromDB(id)
               dbDelSuccess?.value = true
           } catch (e: Exception){
               e.printStackTrace()
               dbDelSuccess?.value = false
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

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}