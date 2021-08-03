package com.example.themoviedatabaseapp.adapter

import com.example.themoviedatabaseapp.model.today.TdResult

interface TVTodayListener {
    fun TVTodItemClickListener(tvItem: TdResult)
}