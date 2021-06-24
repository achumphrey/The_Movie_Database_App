package com.example.themoviedatabaseapp.remote

class Constants {

    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val CURR_ENDPOINT_URL = "tv/on_the_air?api_key=2ca947b36cea2f898bd68e3a64039720"
        const val TODAY_ENDPOINT_URL = "tv/airing_today?api_key=2ca947b36cea2f898bd68e3a64039720"
        const val TV_DETAILS_ENDPOINT_URL = "tv/{tv_id}?api_key=2ca947b36cea2f898bd68e3a64039720"
    }
}