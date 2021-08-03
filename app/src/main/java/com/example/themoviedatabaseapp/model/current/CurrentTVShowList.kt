package com.example.themoviedatabaseapp.model.current


import com.google.gson.annotations.SerializedName

data class CurrentTVShowList(
    @SerializedName("results")
    val results: List<CurResult>
)