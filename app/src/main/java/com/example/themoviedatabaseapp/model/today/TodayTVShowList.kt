package com.example.themoviedatabaseapp.model.today


import com.google.gson.annotations.SerializedName

data class TodayTVShowList(
    @SerializedName("results")
    val results: List<TdResult>
)