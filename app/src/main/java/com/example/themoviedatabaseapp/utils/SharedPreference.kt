package com.example.themoviedatabaseapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.themoviedatabaseapp.MainActivity
import com.example.themoviedatabaseapp.di.TVShowApp
import com.example.themoviedatabaseapp.model.today.TdResult
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList


class SharedPreference {

    private var sharedPreferences: SharedPreferences
    private var context: Context = TVShowApp.getContext()

    companion object {
        const val PREFS_NAME = "FAVOURITE_SHOWS"
        const val FAVOURITES = "TV_SHOW_Favourite"
    }

    init {
        sharedPreferences = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }

    // This four methods are used for maintaining favorites.
    private fun saveFavorites(favouritesList: List<TdResult>) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val gson = Gson()
        val jsonFavourites = gson.toJson(favouritesList)
        editor.putString(FAVOURITES, jsonFavourites)
        editor.apply()
    }

    fun addFavorite(tvShowModel: TdResult) {
        var favoritesList: ArrayList<TdResult> = getFavorites()
        if (favoritesList.isNullOrEmpty())
            favoritesList = ArrayList()
        favoritesList.add(tvShowModel)
        saveFavorites(favoritesList)
    }

    fun removeFavorite(tvShowModel: TdResult) {
        val favouritesList: ArrayList<TdResult> = getFavorites()
        if (favouritesList.isNotEmpty()) {
            favouritesList.remove(tvShowModel)
            saveFavorites(favouritesList)
        }
    }

    fun deleteAllFavorites() {
        val favouritesList: ArrayList<TdResult> = getFavorites()
        if (favouritesList.isNotEmpty()) {
            favouritesList.clear()
            saveFavorites(favouritesList)
        }
    }

    fun checkIfInSharePref(tvShowModel: TdResult): Boolean {
        val favouritesList: ArrayList<TdResult> = getFavorites()
        if (favouritesList.isNotEmpty()) {
            favouritesList.forEach { tvShow: TdResult ->
                if (tvShow == tvShowModel) {
                    return true
                }
            }
        }
        return false
    }

    fun getFavorites(): ArrayList<TdResult> {
        var favouritesList: List<TdResult>

        if (sharedPreferences.contains(FAVOURITES)) {
            val jsonFavorites = sharedPreferences.getString(FAVOURITES, null)
            val gson = Gson()
            val favoriteTVShowArray: Array<TdResult> = gson.fromJson(
                jsonFavorites,
                Array<TdResult>::class.java
            )
            favouritesList = favoriteTVShowArray.toList()
            favouritesList = ArrayList<TdResult>(favouritesList)

        } else return arrayListOf()

        return favouritesList
    }
}