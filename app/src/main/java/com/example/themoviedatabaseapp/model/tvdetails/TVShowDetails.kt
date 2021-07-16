package com.example.themoviedatabaseapp.model.tvdetails


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "tvShow")
data class TVShowDetails(

    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    val name: String,
    @SerializedName("original_language")
    val originalLanguage: String?,
    val genres: List<Genre>?,
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @SerializedName("last_air_date")
    val lastAirDate: String?,
    @SerializedName("last_episode_to_air")
    val lastEpisodeToAir: LastEpisodeToAir?,
    val overview: String?,
    val popularity: Double?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry>?,
    @SerializedName("vote_average")
    val voteAverage: Double?

)