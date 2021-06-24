package com.example.themoviedatabaseapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.adapter.TVCurAdapter
import com.example.themoviedatabaseapp.adapter.TVCurListener
import com.example.themoviedatabaseapp.model.current.Result
import com.example.themoviedatabaseapp.remote.WebClient
import com.example.themoviedatabaseapp.remote.WebServices
import com.example.themoviedatabaseapp.repository.TVRepo
import com.example.themoviedatabaseapp.repository.TVRepoImpl
import com.example.themoviedatabaseapp.viewmodel.TVViewModel
import com.example.themoviedatabaseapp.viewmodel.ViewModelFactory


class TVCurrentlyAiring : Fragment() {

    private lateinit var repo: TVRepo
    private lateinit var webService: WebServices
    private lateinit var tvCurRecyclerView: RecyclerView
    private lateinit var tvProgressBar: ProgressBar
    private lateinit var tvErrorMessage: TextView
 //   private lateinit var tvViewModelFactory: ViewModelFactory
    private lateinit var tvViewModel: TVViewModel
    private lateinit var tvCurAdapter: TVCurAdapter
 //   companion object{const val INTENT_MESSAGE = "message"}
    private val tvCurClickListener: TVCurListener = object : TVCurListener {
        override fun tvCurItemClickListener(itemList: Result) {
            Toast.makeText (context, "We clicked an item", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webService = WebClient().retrofitInstance
        repo = TVRepoImpl(webService)

        tvViewModel = ViewModelProvider(
            this,
            ViewModelFactory(repo)).get(TVViewModel::class.java)

        tvViewModel.TVCurrentFromViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_t_v__currently__airing,
            container,
            false)
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

        tvViewModel.loadingState.observe(viewLifecycleOwner, {
            when (it) {
                TVViewModel.LoadingState.LOADING -> displayProgressbar()
                TVViewModel.LoadingState.SUCCESS -> displayTVList()
                TVViewModel.LoadingState.ERROR -> displayErrorMessage()
                else -> displayErrorMessage()
            }
        })

        tvViewModel.curTVLiveData().observe(viewLifecycleOwner, {
            tvCurAdapter.updateTvCurList(it)
        })

        tvViewModel.errorMessage().observe(viewLifecycleOwner, {
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
}