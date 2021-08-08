package com.example.themoviedatabaseapp.repository

import com.example.themoviedatabaseapp.MainActivity
import com.example.themoviedatabaseapp.database.TVShowDatabase
import com.example.themoviedatabaseapp.model.current.CurrentTVShowList
import com.example.themoviedatabaseapp.model.today.TodayTVShowList
import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails
import com.example.themoviedatabaseapp.remote.WebServices
import javax.inject.Inject

class TVRepoImpl @Inject constructor(private val webServices: WebServices) : TVRepo {

    private val tvDao = TVShowDatabase.getDatabase(MainActivity.getContextOfApplication())?.tvDao()

    override suspend fun getTVCurrent(): CurrentTVShowList {
        return webServices.getCurrentTVList()
    }

    override suspend fun getTVToday(): TodayTVShowList {
        return webServices.getTodayTVList()
    }

    override suspend fun getTVDetail(id: Int): TVShowDetails {
        return webServices.getTVDetails(id)

    }

    override suspend fun addTVToDB(tvShow: TVShowDetails) {
        tvDao!!.insertShow(tvShow)
    }

    override suspend fun getTVFromDB(id: Int): TVShowDetails {
        return tvDao!!.getTvShow(id)
    }

    override suspend fun delTVFromDB(id: Int) {
        return tvDao!!.delTvShow(id)
    }

    override suspend fun checkIfData(id: Int): Int {
        return tvDao!!.count(id)
    }
}