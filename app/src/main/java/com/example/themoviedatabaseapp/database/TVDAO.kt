package com.example.themoviedatabaseapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface TVDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShow(tvShow: TVShowDetails): Completable

    @Query("select * from tvShow where id = :id")
    fun getTvShow(id: Int): Flowable<TVShowDetails>

    @Query("delete from tvShow where id = :id")
    fun delTvShow(id: Int): Completable
}