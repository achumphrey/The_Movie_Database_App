package com.example.themoviedatabaseapp.database

import androidx.room.TypeConverter
import com.example.themoviedatabaseapp.model.tvdetails.Genre
import com.example.themoviedatabaseapp.model.tvdetails.LastEpisodeToAir
import com.example.themoviedatabaseapp.model.tvdetails.ProductionCountry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {

    @TypeConverter
    fun fromGenreListToString(value: List<Genre>): String{
        val gson = Gson()
        val type: Type? = object: TypeToken<List<Genre>>(){}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun fromStringToGenreList(value: String): List<Genre>{
        val gson = Gson()
        val type: Type? = object: TypeToken<List<Genre>>(){}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromProdListToString(value: List<ProductionCountry>): String{
        val gson = Gson()
        val type: Type? = object: TypeToken<List<ProductionCountry>>(){}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun fromStringToProdList(value: String): List<ProductionCountry>{
        val gson = Gson()
        val type: Type? = object: TypeToken<List<ProductionCountry>>(){}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromLastEpisodeToAirToString(value: LastEpisodeToAir): String{
        val gson = Gson()
        val type: Type? = object: TypeToken<LastEpisodeToAir>(){}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun fromStringToLastEpisodeToAir(value: String): LastEpisodeToAir{
        val gson = Gson()
        val type: Type? = object: TypeToken<LastEpisodeToAir>(){}.type
        return gson.fromJson(value, type)
    }
}