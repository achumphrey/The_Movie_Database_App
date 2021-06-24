package com.example.themoviedatabaseapp.remote

import com.example.themoviedatabaseapp.model.current.CurrentTVShowList
import com.example.themoviedatabaseapp.model.today.TodayTVShowList
import io.reactivex.Single
import retrofit2.http.GET

interface WebServices {
    @GET(Constants.CURR_ENDPOINT_URL)
    fun getCurrentTVList(): Single<CurrentTVShowList>

    @GET(Constants.TODAY_ENDPOINT_URL)
    fun getTodayTVList(): Single<TodayTVShowList>

}