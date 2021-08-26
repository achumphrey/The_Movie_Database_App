package com.example.themoviedatabaseapp.model.tvdetails


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "tvShow")
data class TVShowDetails(

    @PrimaryKey
    @SerializedName("id")
    val id: Int?=null,
    val name: String?=null,
    @SerializedName("original_language")
    val originalLanguage: String?=null,
    val genres: List<Genre>?=null,
    @SerializedName("first_air_date")
    val firstAirDate: String?=null,
    @SerializedName("last_air_date")
    val lastAirDate: String?=null,
    @SerializedName("last_episode_to_air")
    val lastEpisodeToAir: LastEpisodeToAir?=null,
    val overview: String?=null,
    val popularity: Double?=null,
    @SerializedName("poster_path")
    val posterPath: String?=null,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry>?=null,
    @SerializedName("vote_average")
    val voteAverage: Double?=null

)