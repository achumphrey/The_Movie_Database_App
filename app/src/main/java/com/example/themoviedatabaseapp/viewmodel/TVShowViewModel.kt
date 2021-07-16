package com.example.themoviedatabaseapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themoviedatabaseapp.model.today.Result
import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails
import com.example.themoviedatabaseapp.repository.TVRepo
import io.reactivex.disposables.CompositeDisposable
import java.net.UnknownHostException

class TVShowViewModel(private val repo: TVRepo) : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()
    private val showTodayTVList: MutableLiveData<List<Result>> = MutableLiveData()
    private val showTVDetails: MutableLiveData<TVShowDetails> = MutableLiveData()
    private val showCurrentTVList: MutableLiveData<List
    <com.example.themoviedatabaseapp.model.current.Result>> = MutableLiveData()
    private val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loadingState = MutableLiveData<LoadingState>()

    private var dBGetSuccess: MutableLiveData<Boolean>? = MutableLiveData()
    private var dBAddSuccess: MutableLiveData<Boolean>? = MutableLiveData()
    private var showFromDb: MutableLiveData<TVShowDetails>? = MutableLiveData()
    private var dbDelSuccess: MutableLiveData<Boolean>? = MutableLiveData()


    fun tvCurrentFromViewModel() {

        loadingState.value = LoadingState.LOADING

        disposable.add(
            repo.getTVCurrent()
                .subscribe({
                    if (it.results.isEmpty()) {
                        errorMessage.value = "No Data Found"
                        loadingState.value = LoadingState.ERROR
                    } else {
                        showCurrentTVList.value = it.results
                        loadingState.value = LoadingState.SUCCESS
                    }
                }, {
                    it.printStackTrace()
                    when (it) {
                        is UnknownHostException -> errorMessage.value = "No Network!"
                        else -> errorMessage.value = it.localizedMessage
                    }
                    loadingState.value = LoadingState.ERROR
                })
        )
    }

    fun tvTodayFromViewModel() {

        loadingState.value = LoadingState.LOADING

        disposable.add(
            repo.getTVToday()
                .subscribe({
                    if (it.results.isEmpty()) {
                        errorMessage.value = "No Data Found"
                        loadingState.value = LoadingState.ERROR
                    } else {
                        showTodayTVList.value = it.results
                        loadingState.value = LoadingState.SUCCESS
                    }

                }, {
                    it.printStackTrace()
                    when (it) {
                        is UnknownHostException -> errorMessage.value = "No Network!"
                        else -> errorMessage.value = it.localizedMessage
                    }
                    loadingState.value = LoadingState.ERROR
                })
        )
    }

    fun fetchTVDetails(id: Int) {

        loadingState.value = LoadingState.LOADING

        disposable.add(
            repo.getTVDetail(id).subscribe({
                if (it == null) {
                    errorMessage.value = "No Data Found"
                    loadingState.value = LoadingState.ERROR
                } else {
                    loadingState.value = LoadingState.SUCCESS
                    showTVDetails.value = it
                    addShowToDB(it)
                }
            }, {
                it.printStackTrace()
                when (it) {
                    is UnknownHostException -> errorMessage.value = "No Network!"
                    else -> errorMessage.value = it.localizedMessage
                }
                loadingState.value = LoadingState.ERROR
            })
        )
    }

    private fun addShowToDB(tvShow: TVShowDetails) {
        disposable.add(
            repo.addTVToDB(tvShow)
                .subscribe({
                    dBAddSuccess?.value = true
                }, {
                    it.printStackTrace()
                    dBAddSuccess?.value = false
                })
        )
    }

    fun getShowFromDB(id: Int) {
        loadingState.value = LoadingState.LOADING
        disposable.add(
            repo.getTVFromDB(id).subscribe({
                if (it == null){
                    errorMessage.value = "No Data Found In DB"
                    loadingState.value = LoadingState.ERROR
                }else{
                showFromDb?.postValue(it)
                dBGetSuccess?.value = true
                loadingState.value = LoadingState.SUCCESS
                }
            }, {
                it.printStackTrace()
                dBGetSuccess?.value = false
                loadingState.value = LoadingState.ERROR
            })
        )
    }

    fun delShowFromDB(id: Int) {
        disposable.add(
            repo.delTVFromDB(id).subscribe({
                dbDelSuccess?.value = true
            }, {
                it.printStackTrace()
                dbDelSuccess?.value = false
            })
        )
    }

    fun onShowFromDB(): MutableLiveData<TVShowDetails>? {
        return showFromDb
    }

    enum class LoadingState {
        LOADING,
        SUCCESS,
        ERROR
    }

    fun curTVLiveData(): MutableLiveData<List<com.example.themoviedatabaseapp.model.current.Result>> {
        return showCurrentTVList
    }

    fun todayTVLiveData(): MutableLiveData<List<Result>> {
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