package com.example.themoviedatabaseapp.repository

import com.example.themoviedatabaseapp.model.TVDetails.TVShowDetails
import com.example.themoviedatabaseapp.model.current.CurrentTVShowList
import com.example.themoviedatabaseapp.model.today.TodayTVShowList
import com.example.themoviedatabaseapp.remote.WebServices
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TVRepoImpl @Inject constructor(private val webServices: WebServices): TVRepo {

    override fun getTVCurrent(): Single<CurrentTVShowList> {
        return webServices.getCurrentTVList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getTVToday(): Single<TodayTVShowList> {
        return webServices.getTodayTVList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getTVDetail(id: Int): Single<TVShowDetails> {
        return webServices.getTVDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}