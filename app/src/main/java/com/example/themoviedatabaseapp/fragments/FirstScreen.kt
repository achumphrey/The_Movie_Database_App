package com.example.themoviedatabaseapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.themoviedatabaseapp.R


class FirstScreen : Fragment() {

    private lateinit var fragTVCurAiring: TVCurrentlyAiring
    private lateinit var fragTVAiringToday: TVAiringToday
    private lateinit var tvFirstScren: TextView
    private lateinit var btnCurr: Button
    private lateinit var btnAirToday: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_first_screen,
            container,
            false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvFirstScren = view.findViewById(R.id.firstScreenInstruction)
        btnAirToday = view.findViewById(R.id.btnAiringToday)
        btnCurr = view.findViewById(R.id.btnCurrentAiring)

        btnAirToday.setOnClickListener {
            whenButtonIsClicked(it)
        }
        btnCurr.setOnClickListener {
            whenButtonIsClicked(it)
        }
    }

    private fun whenButtonIsClicked(view: View) {
        when (view.id) {
            R.id.btnAiringToday -> {
                fragTVAiringToday = TVAiringToday()
                val fragMgr: FragmentManager = activity?.supportFragmentManager!!
                val fragTrans: FragmentTransaction = fragMgr.beginTransaction()
                fragTrans.replace(R.id.fragContainer, fragTVAiringToday, "Air_Today_Frag")
                fragTrans.addToBackStack(fragTVAiringToday.tag)
                fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                fragTrans.commit()
            }

            R.id.btnCurrentAiring -> {
                fragTVCurAiring = TVCurrentlyAiring()
                val fragMgr: FragmentManager = activity?.supportFragmentManager!!
                val fragTrans: FragmentTransaction = fragMgr.beginTransaction()
                fragTrans.replace(R.id.fragContainer, fragTVCurAiring, "Cur_Air_Frag")
                fragTrans.addToBackStack(fragTVCurAiring.tag)
                fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                fragTrans.commit()
            }
        }
    }
}