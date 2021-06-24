package com.example.themoviedatabaseapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.themoviedatabaseapp.repository.TVRepo

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repo: TVRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TVViewModel(repo) as T
    }
}