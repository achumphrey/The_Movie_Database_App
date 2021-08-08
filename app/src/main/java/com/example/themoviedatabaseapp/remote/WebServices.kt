package com.example.themoviedatabaseapp.remote

import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails
import com.example.themoviedatabaseapp.model.current.CurrentTVShowList
import com.example.themoviedatabaseapp.model.today.TodayTVShowList
import retrofit2.http.GET
import retrofit2.http.Path

interface WebServices {
    @GET(Constants.CURR_ENDPOINT_URL)
    suspend fun getCurrentTVList(): CurrentTVShowList

    @GET(Constants.TODAY_ENDPOINT_URL)
    suspend fun getTodayTVList(): TodayTVShowList

    @GET(Constants.TV_DETAILS_ENDPOINT_URL)
    suspend fun getTVDetails(@Path("tv_id") tvId: Int): TVShowDetails

}