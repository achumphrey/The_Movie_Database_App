package com.example.themoviedatabaseapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themoviedatabaseapp.model.current.CurResult
import com.example.themoviedatabaseapp.model.today.TdResult
import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails
import com.example.themoviedatabaseapp.repository.TVRepo
import io.reactivex.disposables.CompositeDisposable
import java.net.UnknownHostException

class TVShowViewModel(private val repo: TVRepo) : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()
    private val showTodayTVList: MutableLiveData<List<TdResult>> = MutableLiveData()
    private val showTVDetails: MutableLiveData<TVShowDetails> = MutableLiveData()
    private val showCurrentTVList: MutableLiveData<List<CurResult>> = MutableLiveData()
    private val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loadingState = MutableLiveData<LoadingState>()

    private var dBAddSuccess: MutableLiveData<Boolean>? = MutableLiveData()
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

    private fun fetchTVDetails(id: Int) {

        loadingState.value = LoadingState.LOADING

        disposable.add(
            repo.getTVDetail(id).subscribe({
                when (it) {
                    null -> {
                        errorMessage.value = "No Data Found"
                        loadingState.value = LoadingState.ERROR
                    }
                    else -> {
                        loadingState.value = LoadingState.SUCCESS
                        showTVDetails.value = it
                        addShowToDB(it)
                    }
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

    fun getCount(id: Int){
        disposable.add(
            repo.checkIfData(id).subscribe({
                  if (it > 0)
                      getShowFromDB(id)
                  else
                      fetchTVDetails(id)

            },{
                it.printStackTrace()
            })
        )
    }

    private fun getShowFromDB(id: Int) {

        loadingState.value = LoadingState.LOADING

        disposable.add(
            repo.getTVFromDB(id).subscribe({showObject ->
                when {
                    showObject != null -> {
                        showTVDetails.value = showObject
                        loadingState.value = LoadingState.SUCCESS
                    }
                    else -> {
                        errorMessage.value = "No Data Found In DB"
                        loadingState.value = LoadingState.ERROR
                    }
                }
            }, {
                it.printStackTrace()
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