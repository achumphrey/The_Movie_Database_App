package com.example.themoviedatabaseapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.themoviedatabaseapp.MainActivity
import com.example.themoviedatabaseapp.model.today.Result
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList


class SharedPreference {

    private var sharedPreferences: SharedPreferences
    private var context: Context = MainActivity.getContextOfApplication()

    companion object{
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
    private fun saveFavorites(favouritesList: List<Result>) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val gson = Gson()
        val jsonFavourites = gson.toJson(favouritesList)
        editor.putString(FAVOURITES, jsonFavourites)
        editor.apply()
    }

    fun addFavorite(tvShowModel: Result) {
        var favoritesList: ArrayList<Result> = getFavorites()
        if (favoritesList.isNullOrEmpty())
            favoritesList = ArrayList()
        favoritesList.add(tvShowModel)
        saveFavorites(favoritesList)
    }

    fun removeFavorite(tvShowModel: Result) {
        val favouritesList: ArrayList<Result> = getFavorites()
        if (favouritesList.isNotEmpty()) {
            favouritesList.remove(tvShowModel)
            saveFavorites(favouritesList)
        }
    }

    fun checkIfInSharePref(tvShowModel: Result): Boolean  {
        val favouritesList: ArrayList<Result> = getFavorites()
        if (favouritesList.isNotEmpty()) {
            favouritesList.forEach { tvShow: Result ->
                if (tvShow == tvShowModel){
                    return true
                }
            }
        }
        return false
    }

    fun getFavorites(): ArrayList<Result> {
        var favouritesList: List<Result>

        if (sharedPreferences.contains(FAVOURITES)) {
            val jsonFavorites = sharedPreferences.getString(FAVOURITES, null)
            val gson = Gson()
            val favoriteTVShowArray: Array<Result> = gson.fromJson(
                jsonFavorites,
                Array<Result>::class.java
            )
            favouritesList = favoriteTVShowArray.toList()
            favouritesList = ArrayList<Result>(favouritesList)

        } else return arrayListOf()

        return favouritesList
    }
}