package com.example.themoviedatabaseapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.TVDetailsActivity
import com.example.themoviedatabaseapp.adapter.TVTodayAdapter
import com.example.themoviedatabaseapp.adapter.TVTodayListener
import com.example.themoviedatabaseapp.di.TVShowApp
import com.example.themoviedatabaseapp.model.today.Result
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModel
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModelFactory
import javax.inject.Inject


class TVAiringToday : Fragment() {

    private lateinit var tvTdRecyclerView: RecyclerView
    private lateinit var tvProgressBar: ProgressBar
    private lateinit var tvErrorMessage: TextView

    @Inject
    lateinit var tvShowViewModelFactory: TVShowViewModelFactory
    private lateinit var tvShowViewModel: TVShowViewModel
    private lateinit var tvTdAdapter: TVTodayAdapter

    companion object {
        const val INTENT_MESSAGE = "message"
    }

    private val tvTdClickListener: TVTodayListener = object : TVTodayListener {

        override fun TVTodItemClickListener(tvItem: Result) {
            val intent = Intent(context, TVDetailsActivity::class.java)
            intent.putExtra(INTENT_MESSAGE, tvItem.id)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TVShowApp.getTVShowComponent().inject(this)

        tvShowViewModel =
            ViewModelProvider(this, tvShowViewModelFactory)
                .get(TVShowViewModel::class.java)

        tvShowViewModel.TVTodayFromViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_t_v__airing__today,
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

        tvTdRecyclerView = requireView().findViewById(R.id.recyViewTvAiringToday)
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

        tvShowViewModel.TodayTVLiveData().observe(viewLifecycleOwner, {
            tvTdAdapter.updateTvTdList(it)
        })

        tvShowViewModel.errorMessage().observe(viewLifecycleOwner, {
            tvErrorMessage.text = it
        })
    }

    private fun setupRecyclerView() {
        tvTdRecyclerView.layoutManager = LinearLayoutManager(context)
        tvTdAdapter = TVTodayAdapter(mutableListOf(), tvTdClickListener)
        tvTdRecyclerView.adapter = tvTdAdapter
    }

    private fun displayTVList() {
        tvErrorMessage.visibility = View.GONE
        tvTdRecyclerView.visibility = View.VISIBLE
        tvProgressBar.visibility = View.GONE
    }

    private fun displayErrorMessage() {
        tvErrorMessage.visibility = View.VISIBLE
        tvTdRecyclerView.visibility = View.GONE
        tvProgressBar.visibility = View.GONE
    }

    private fun displayProgressbar() {
        tvProgressBar.visibility = View.VISIBLE
        tvTdRecyclerView.visibility = View.GONE
        tvErrorMessage.visibility = View.GONE
    }
}