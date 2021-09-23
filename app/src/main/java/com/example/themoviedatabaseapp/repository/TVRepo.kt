package com.example.themoviedatabaseapp.repository

import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails
import com.example.themoviedatabaseapp.model.current.CurrentTVShowList
import com.example.themoviedatabaseapp.model.today.TodayTVShowList

interface TVRepo {

    suspend fun getTVCurrent(): CurrentTVShowList
    suspend fun getTVToday(): TodayTVShowList
    suspend fun getTVDetail(id: Int): TVShowDetails
    suspend fun addTVToDB(tvShow: TVShowDetails)
    suspend fun getTVFromDB(id: Int): TVShowDetails
    suspend fun delTVFromDB(id: Int): Int
    suspend fun checkIfData(id: Int): Int
}