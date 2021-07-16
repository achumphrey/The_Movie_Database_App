package com.example.themoviedatabaseapp.di

import android.app.Application

class TVShowApp: Application() {

    companion object{
        private lateinit var tvShowComponent: TVShowComponent
        fun getTVShowComponent() = tvShowComponent
    }

    override fun onCreate() {
        super.onCreate()
        tvShowComponent = buildTVShowComponent()
    }

    private fun buildTVShowComponent(): TVShowComponent{
        return DaggerTVShowComponent.builder()
            .tVRepoModule(TVRepoModule())
            .tVShowNetworkModule(TVShowNetworkModule())
            .build()
    }
}