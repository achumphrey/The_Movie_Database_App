package com.example.themoviedatabaseapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.themoviedatabaseapp.fragments.FirstScreen
import com.example.themoviedatabaseapp.fragments.TVAiringToday
import com.example.themoviedatabaseapp.fragments.TVCurrentlyAiring


class MainActivity : AppCompatActivity() {

    var fragTVCurAiring: TVCurrentlyAiring? = null
    var fragTVAiringToday: TVAiringToday? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstFrag = FirstScreen()
        val fragMgr: FragmentManager = supportFragmentManager
        val fragTrans: FragmentTransaction = fragMgr.beginTransaction()
        fragTrans.replace(R.id.fragContainer, firstFrag)
        fragTrans.addToBackStack(null)
        fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragTrans.commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.tvcurAiring -> {
                fragTVCurAiring = TVCurrentlyAiring()
                val fragMgr: FragmentManager = supportFragmentManager
                val fragTrans: FragmentTransaction = fragMgr.beginTransaction()
                fragTrans.replace(R.id.fragContainer, fragTVCurAiring!!, "Cur_Air_Frag")
                fragTrans.addToBackStack(fragTVCurAiring!!.tag)
                fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                fragTrans.commit()
            }

            R.id.tvAiringToday -> {
                fragTVAiringToday = TVAiringToday()
                val fragMgr: FragmentManager = supportFragmentManager
                val fragTrans: FragmentTransaction = fragMgr.beginTransaction()
                fragTrans.replace(R.id.fragContainer, fragTVAiringToday!!, "Air_Today_Frag")
                fragTrans.addToBackStack(fragTVAiringToday!!.tag)
                fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                fragTrans.commit()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}