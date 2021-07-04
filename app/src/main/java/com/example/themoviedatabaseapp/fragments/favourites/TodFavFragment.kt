package com.example.themoviedatabaseapp.fragments.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.adapter.favourites.TodFavAdapter
import com.example.themoviedatabaseapp.adapter.favourites.TodFavListener
import com.example.themoviedatabaseapp.model.today.Result
import com.example.themoviedatabaseapp.utils.SharedPreference

class TodFavFragment : Fragment() {

    private lateinit var todFavRecyclerView: RecyclerView
    private lateinit var todFavAdapter: TodFavAdapter
    private lateinit var errorMessage: TextView
    private lateinit var progbar: ProgressBar
    var todDataArrayList: ArrayList<Result> = arrayListOf()

    private val tvTodFavClickListener: TodFavListener = object : TodFavListener {
        override fun tdFavItemClickListener(itemList: Result) {
            callDetailsFragment(itemList.id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todDataArrayList = SharedPreference().getFavorites()
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_tod_fav,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todFavRecyclerView = requireView().findViewById(R.id.recyViewFavAiringToday)
        errorMessage = requireView().findViewById(R.id.tvFavTodErrorMessage)
        progbar = requireView().findViewById(R.id.tvFavTodProgressBar)

        if (todDataArrayList.isNullOrEmpty()) {
            todFavRecyclerView.visibility = View.GONE
            errorMessage.text = String.format("No Favourites to display")
            errorMessage.visibility = View.VISIBLE
            progbar.visibility = View.GONE
        } else {
            todFavRecyclerView.visibility = View.VISIBLE
            progbar.visibility = View.GONE
            errorMessage.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        todFavRecyclerView.layoutManager = LinearLayoutManager(context)
        todFavAdapter = TodFavAdapter(mutableListOf(), tvTodFavClickListener)
        todFavRecyclerView.adapter = todFavAdapter
        todFavAdapter.updateTdFavList(todDataArrayList)
    }

    private fun callDetailsFragment(id: Int) {
        val directions = TodFavFragmentDirections.actionTodFavFragmentToTVDetails(id)
            findNavController().navigate(directions)
    }
}