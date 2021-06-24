package com.example.themoviedatabaseapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.themoviedatabaseapp.fragments.TV_Airing_Today
import com.example.themoviedatabaseapp.fragments.TVCurrentlyAiring


class MainActivity : AppCompatActivity() {

    var fragTVCurAiring : TVCurrentlyAiring? = null
    var fragTVAiringToday : TV_Airing_Today? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
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
                fragTVAiringToday = TV_Airing_Today()
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