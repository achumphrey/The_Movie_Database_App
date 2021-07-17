package com.example.themoviedatabaseapp.repository

import com.example.themoviedatabaseapp.MainActivity
import com.example.themoviedatabaseapp.database.TVShowDatabase
import com.example.themoviedatabaseapp.model.current.CurrentTVShowList
import com.example.themoviedatabaseapp.model.today.TodayTVShowList
import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails
import com.example.themoviedatabaseapp.remote.WebServices
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TVRepoImpl @Inject constructor(private val webServices: WebServices) : TVRepo {

    private val tvDao = TVShowDatabase.getDatabase(MainActivity.getContextOfApplication())?.tvDao()

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

    override fun addTVToDB(tvShow: TVShowDetails): Completable {
        return tvDao!!.insertShow(tvShow)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getTVFromDB(id: Int): Flowable<TVShowDetails> {
        return tvDao!!.getTvShow(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun delTVFromDB(id: Int): Completable {
        return tvDao!!.delTvShow(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun checkIfData(id: Int): Single<Int> {
        return tvDao!!.count(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}