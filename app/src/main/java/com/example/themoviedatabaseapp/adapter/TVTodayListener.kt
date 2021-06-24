package com.example.themoviedatabaseapp.adapter

import com.example.themoviedatabaseapp.model.today.Result

interface TVTodayListener {
    fun TVTodItemClickListener(tvItem: Result)
}