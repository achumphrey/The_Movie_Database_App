package com.example.themoviedatabaseapp.remote

import com.example.themoviedatabaseapp.model.TVDetails.TVShowDetails
import com.example.themoviedatabaseapp.model.current.CurrentTVShowList
import com.example.themoviedatabaseapp.model.today.TodayTVShowList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface WebServices {
    @GET(Constants.CURR_ENDPOINT_URL)
    fun getCurrentTVList(): Single<CurrentTVShowList>

    @GET(Constants.TODAY_ENDPOINT_URL)
    fun getTodayTVList(): Single<TodayTVShowList>

    @GET(Constants.TV_DETAILS_ENDPOINT_URL)
    fun getTVDetails(@Path("tv_id") tvId: Int): Single<TVShowDetails>

}