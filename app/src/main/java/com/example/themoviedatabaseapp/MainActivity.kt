package com.example.themoviedatabaseapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.themoviedatabaseapp.fragments.FirstScreen


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        applyFirstScreen()
    }

    private fun applyFirstScreen() {
        val firstFrag = FirstScreen()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragContainer, firstFrag, "firstFrag")
            .addToBackStack(firstFrag.tag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }


}