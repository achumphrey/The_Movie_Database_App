package com.example.themoviedatabaseapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.themoviedatabaseapp.R


class FirstScreen : Fragment() {

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
            false
        )
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
                callToTVAiringToday()
            }

            R.id.btnCurrentAiring -> {
                callToTVCurAiring()
            }
        }
    }

    private fun callToTVAiringToday() {
        val directions = FirstScreenDirections.actionFirstScreenToTVAiringToday()
        findNavController().navigate(directions)
    }

    private fun callToTVCurAiring() {
        val directions = FirstScreenDirections.actionFirstScreenToTVCurrentlyAiring()
        findNavController().navigate(directions)
    }
}