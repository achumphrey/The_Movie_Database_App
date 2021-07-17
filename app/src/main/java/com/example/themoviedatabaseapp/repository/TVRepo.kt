package com.example.themoviedatabaseapp.repository

import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails
import com.example.themoviedatabaseapp.model.current.CurrentTVShowList
import com.example.themoviedatabaseapp.model.today.TodayTVShowList
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface TVRepo {

    fun getTVCurrent(): Single<CurrentTVShowList>
    fun getTVToday(): Single<TodayTVShowList>
    fun getTVDetail(id: Int): Single<TVShowDetails>
    fun addTVToDB(tvShow: TVShowDetails): Completable
    fun getTVFromDB(id: Int): Flowable<TVShowDetails>
    fun delTVFromDB(id: Int): Completable
    fun checkIfData(id: Int): Single<Int>
}