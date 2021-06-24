package com.example.themoviedatabaseapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.themoviedatabaseapp.R


/**
 * A simple [Fragment] subclass.
 * Use the [TV_Airing_Today.newInstance] factory method to
 * create an instance of this fragment.
 */
class TV_Airing_Today : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_t_v__airing__today,
            container,
            false)
    }


}