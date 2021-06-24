package com.example.themoviedatabaseapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themoviedatabaseapp.model.TVDetails.TodayTVShowDetails
import com.example.themoviedatabaseapp.model.today.Result
import com.example.themoviedatabaseapp.repository.TVRepo
import io.reactivex.disposables.CompositeDisposable
import java.net.UnknownHostException

class TVViewModel(private val repo: TVRepo) : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()
    private val showTodayTVList: MutableLiveData<List<Result>> = MutableLiveData()
    private val showTVDetails: MutableLiveData<TodayTVShowDetails> = MutableLiveData()
    private val showCurrentTVList: MutableLiveData<List
    <com.example.themoviedatabaseapp.model.current.Result>> = MutableLiveData()
    private val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loadingState = MutableLiveData<LoadingState>()


    fun TVCurrentFromViewModel() {

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

    fun TVTodayFromViewModel() {

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

        disposable.add(
            repo.getTVDetail(id).subscribe({
                if (it == null) {
                    errorMessage.value = "No Data Found"
                    loadingState.value = LoadingState.ERROR
                } else {
                    loadingState.value = LoadingState.SUCCESS
                    showTVDetails.value = it
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

    enum class LoadingState {
        LOADING,
        SUCCESS,
        ERROR
    }

    fun curTVLiveData(): MutableLiveData<List<com.example.themoviedatabaseapp.model.current.Result>> {
        return showCurrentTVList
    }

    fun TodayTVLiveData(): MutableLiveData<List<Result>> {
        return showTodayTVList
    }

    fun errorMessage(): MutableLiveData<String> {
        return errorMessage
    }

    fun tvDetails(): MutableLiveData<TodayTVShowDetails>{
        return showTVDetails
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

}