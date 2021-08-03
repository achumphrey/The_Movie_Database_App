package com.example.themoviedatabaseapp.adapter.favourites

import com.example.themoviedatabaseapp.model.today.TdResult

interface TodFavListener {
    fun tdFavItemClickListener(itemList: TdResult)
}