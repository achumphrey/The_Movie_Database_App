package com.example.themoviedatabaseapp.di

import com.example.themoviedatabaseapp.TVDetailsActivity
import com.example.themoviedatabaseapp.fragments.TVAiringToday
import com.example.themoviedatabaseapp.fragments.TVCurrentlyAiring
import com.example.themoviedatabaseapp.fragments.TVDetails
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TVRepoModule::class, TVShowNetworkModule::class])
interface TVShowComponent {

    fun inject(tvDetails: TVDetails)
    fun inject(tvDetailsActivity: TVDetailsActivity)
    fun inject(tvAiringToday: TVAiringToday)
    fun inject(tvCurrentlyAiring: TVCurrentlyAiring)
}