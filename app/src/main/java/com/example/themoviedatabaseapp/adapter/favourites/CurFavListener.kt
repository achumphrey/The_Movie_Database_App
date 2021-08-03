package com.example.themoviedatabaseapp.adapter.favourites

import com.example.themoviedatabaseapp.model.current.CurResult

interface CurFavListener {
    fun curFavItemClickListener(itemList: CurResult)
}