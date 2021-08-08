package com.example.themoviedatabaseapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails

@Dao
interface TVDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShow(tvShow: TVShowDetails)

    @Query("select * from tvShow where id = :id")
    suspend fun getTvShow(id: Int): TVShowDetails

    @Query("delete from tvShow where id = :id")
    suspend fun delTvShow(id: Int)

    @Query("select COUNT() from tvShow where id = :id")
    suspend fun count(id: Int): Int
}