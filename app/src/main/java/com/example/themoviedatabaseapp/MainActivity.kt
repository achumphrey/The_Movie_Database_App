package com.example.themoviedatabaseapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    companion object{
        private lateinit var contextOfApplication: Context

        fun getContextOfApplication(): Context {
            return contextOfApplication
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contextOfApplication = applicationContext
    }
}