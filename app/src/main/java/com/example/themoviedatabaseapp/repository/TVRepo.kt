package com.example.themoviedatabaseapp.repository

import com.example.themoviedatabaseapp.model.TVDetails.TodayTVShowDetails
import com.example.themoviedatabaseapp.model.current.CurrentTVShowList
import com.example.themoviedatabaseapp.model.today.TodayTVShowList
import io.reactivex.Single

interface TVRepo {

    fun getTVCurrent(): Single<CurrentTVShowList>
    fun getTVToday(): Single<TodayTVShowList>
    fun getTVDetail(id: Int): Single<TodayTVShowDetails>
}