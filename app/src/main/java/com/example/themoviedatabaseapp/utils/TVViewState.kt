package com.example.themoviedatabaseapp.utils

import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails

class TVViewState {
    sealed class ViewState
    object ViewLoading: ViewState()
    data class Success(val tvDetails: TVShowDetails): ViewState()
    data class Error(val message: String): ViewState()
}