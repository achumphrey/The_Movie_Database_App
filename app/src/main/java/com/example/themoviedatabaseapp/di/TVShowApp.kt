package com.example.themoviedatabaseapp.di

import android.app.Application
import android.content.Context

class TVShowApp: Application() {

    companion object{
        private lateinit var tvShowComponent: TVShowComponent
        private lateinit var context: Application
        fun getTVShowComponent() = tvShowComponent
        fun getContext(): Context = context.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        tvShowComponent = buildTVShowComponent()
        context = this
    }

    private fun buildTVShowComponent(): TVShowComponent{
        return DaggerTVShowComponent.builder()
            .tVRepoModule(TVRepoModule())
            .tVShowNetworkModule(TVShowNetworkModule())
            .build()
    }
}