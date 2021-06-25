package com.example.themoviedatabaseapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.themoviedatabaseapp.repository.TVRepo

@Suppress("UNCHECKED_CAST")
class TVShowViewModelFactory(private val repo: TVRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TVShowViewModel(repo) as T
    }
}