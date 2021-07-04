package com.example.themoviedatabaseapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.adapter.TVCurAdapter
import com.example.themoviedatabaseapp.adapter.TVCurListener
import com.example.themoviedatabaseapp.di.TVShowApp
import com.example.themoviedatabaseapp.model.current.Result
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModel
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModelFactory
import javax.inject.Inject


class TVCurrentlyAiring : Fragment() {

    private lateinit var tvCurRecyclerView: RecyclerView
    private lateinit var tvProgressBar: ProgressBar
    private lateinit var tvErrorMessage: TextView

    @Inject
    lateinit var tvShowViewModelFactory: TVShowViewModelFactory
    private lateinit var tvShowViewModel: TVShowViewModel
    private lateinit var tvCurAdapter: TVCurAdapter

    private val tvCurClickListener: TVCurListener = object : TVCurListener {
        override fun tvCurItemClickListener(itemList: Result) {
            callDetailsFragment(itemList.id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        TVShowApp.getTVShowComponent().inject(this)

        tvShowViewModel =
            ViewModelProvider(this, tvShowViewModelFactory)
                .get(TVShowViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        tvShowViewModel.TVCurrentFromViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_t_v__currently__airing,
            container,
            false
        )
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvCurRecyclerView = requireView().findViewById(R.id.recyViewTvCurAiring)
        tvErrorMessage = requireView().findViewById(R.id.tvErrorMessage)
        tvProgressBar = requireView().findViewById(R.id.tvProgressBar)

        tvShowViewModel.loadingState.observe(viewLifecycleOwner, {
            when (it) {
                TVShowViewModel.LoadingState.LOADING -> displayProgressbar()
                TVShowViewModel.LoadingState.SUCCESS -> displayTVList()
                TVShowViewModel.LoadingState.ERROR -> displayErrorMessage()
                else -> displayErrorMessage()
            }
        })

        tvShowViewModel.curTVLiveData().observe(viewLifecycleOwner, {
            tvCurAdapter.updateTvCurList(it)
        })

        tvShowViewModel.errorMessage().observe(viewLifecycleOwner, {
            tvErrorMessage.text = it
        })
    }

    private fun setupRecyclerView() {
        tvCurRecyclerView.layoutManager = LinearLayoutManager(context)
        tvCurAdapter = TVCurAdapter(mutableListOf(), tvCurClickListener)
        tvCurRecyclerView.adapter = tvCurAdapter
    }

    private fun displayTVList() {
        tvErrorMessage.visibility = View.GONE
        tvCurRecyclerView.visibility = View.VISIBLE
        tvProgressBar.visibility = View.GONE
    }

    private fun displayErrorMessage() {
        tvErrorMessage.visibility = View.VISIBLE
        tvCurRecyclerView.visibility = View.GONE
        tvProgressBar.visibility = View.GONE
    }

    private fun displayProgressbar() {
        tvProgressBar.visibility = View.VISIBLE
        tvCurRecyclerView.visibility = View.GONE
        tvErrorMessage.visibility = View.GONE
    }

    private fun callDetailsFragment(id: Int) {
        val directions = TVCurrentlyAiringDirections.actionTVCurrentlyAiringToTVDetails(id)
        findNavController().navigate(directions)
    }

    private fun callToCurFavFragment(){
        val directions = TVCurrentlyAiringDirections.actionTVCurrentlyAiringToCurFavFragment()
        findNavController().navigate(directions)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.tvcurFav -> {
                callToCurFavFragment()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}