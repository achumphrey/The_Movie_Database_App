package com.example.themoviedatabaseapp.adapter.favourites

import com.example.themoviedatabaseapp.model.current.Result

interface CurFavListener {
    fun curFavItemClickListener(itemList: Result)
}