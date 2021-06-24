package com.example.themoviedatabaseapp.repository

import com.example.themoviedatabaseapp.model.TVDetails.TodayTVShowDetails
import com.example.themoviedatabaseapp.model.current.CurrentTVShowList
import com.example.themoviedatabaseapp.model.today.TodayTVShowList
import com.example.themoviedatabaseapp.remote.WebServices
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TVRepoImpl(private val webServices: WebServices): TVRepo {

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

    override fun getTVDetail(id: Int): Single<TodayTVShowDetails> {
        return webServices.getTVDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}