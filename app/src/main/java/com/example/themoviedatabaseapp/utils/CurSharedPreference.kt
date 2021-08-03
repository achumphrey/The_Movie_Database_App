package com.example.themoviedatabaseapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.themoviedatabaseapp.MainActivity
import com.example.themoviedatabaseapp.model.current.CurResult
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList


class CurSharedPreference {

    private var curSharePreference: SharedPreferences
    private var context: Context = MainActivity.getContextOfApplication()

    companion object {
        const val PREFS_NAME = "CUR_FAVOURITE_SHOWS"
        const val FAVOURITES = "CUR_SHOW_Favourite"
    }

    init {
        curSharePreference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // This four methods are used for maintaining favorites.
    private fun saveFavorites(favouritesList: List<CurResult>) {
        val editor: SharedPreferences.Editor = curSharePreference.edit()
        val gson = Gson()
        val jsonFavourites = gson.toJson(favouritesList)
        editor.putString(FAVOURITES, jsonFavourites)
        editor.apply()
    }

    fun addFavorite(tvShowModel: CurResult) {
        var favoritesList: ArrayList<CurResult> = getFavorites()
        if (favoritesList.isNullOrEmpty())
            favoritesList = ArrayList()
        favoritesList.add(tvShowModel)
        saveFavorites(favoritesList)
    }

    fun removeFavorite(tvShowModel: CurResult) {
        val favouritesList: ArrayList<CurResult> = getFavorites()
        if (favouritesList.isNotEmpty()) {
            favouritesList.remove(tvShowModel)
            saveFavorites(favouritesList)
        }
    }

    fun deleteAllFavorites() {
        val favouritesList: ArrayList<CurResult> = getFavorites()
        if (favouritesList.isNotEmpty()) {
            favouritesList.clear()
            saveFavorites(favouritesList)
        }
    }

    fun checkIfInSharePref(tvShowModel: CurResult): Boolean {
        val favouritesList: ArrayList<CurResult> = getFavorites()
        if (favouritesList.isNotEmpty()) {
            favouritesList.forEach { tvShow: CurResult ->
                if (tvShow == tvShowModel) {
                    return true
                }
            }
        }
        return false
    }

    fun getFavorites(): ArrayList<CurResult> {
        var favouritesList: List<CurResult>

        if (curSharePreference.contains(FAVOURITES)) {
            val jsonFavorites = curSharePreference.getString(FAVOURITES, null)
            val gson = Gson()
            val favoriteTVShowArray: Array<CurResult> = gson.fromJson(
                jsonFavorites,
                Array<CurResult>::class.java
            )
            favouritesList = favoriteTVShowArray.toList()
            favouritesList = ArrayList<CurResult>(favouritesList)

        } else return arrayListOf()

        return favouritesList
    }
}