package com.example.themoviedatabaseapp.di

import android.app.Application
import com.example.themoviedatabaseapp.remote.WebServices
import com.example.themoviedatabaseapp.repository.TVRepo
import com.example.themoviedatabaseapp.repository.TVRepoImpl
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TVRepoModule {

    @Singleton
    @Provides
    fun provideViewModelFactory(repo: TVRepo): TVShowViewModelFactory{
        return  TVShowViewModelFactory(repo)
    }

    @Singleton
    @Provides
    fun provideTVShowRepository(webServices: WebServices): TVRepo{
        return TVRepoImpl(webServices)
    }

    @Singleton
    @Provides
    fun provideApplicationContext(): Application = Application()
}