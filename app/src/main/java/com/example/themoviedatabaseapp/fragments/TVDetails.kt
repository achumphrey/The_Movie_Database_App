package com.example.themoviedatabaseapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.databinding.FragmentTVDetailsBinding
import com.example.themoviedatabaseapp.di.TVShowApp
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModel
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModelFactory
import javax.inject.Inject

class TVDetails : Fragment() {

    private var _binding: FragmentTVDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var tvShowViewModelFactory: TVShowViewModelFactory
    private lateinit var tvShowViewModel: TVShowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TVShowApp.getTVShowComponent().inject(this)

        val args: TVDetailsArgs by navArgs()
        val tvId: Int = args.itemID

        tvShowViewModel = ViewModelProvider(
                this,
                tvShowViewModelFactory)
                .get(TVShowViewModel::class.java)

        tvShowViewModel.fetchTVDetails(tvId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_t_v_details,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvShowViewModel.tvDetails().observe(viewLifecycleOwner, {
            binding.tvDetailObject = it

        })

        tvShowViewModel.errorMessage().observe(viewLifecycleOwner, {
            binding.tvDetailsErrorMessage.text = it
        })

        tvShowViewModel.loadingState.observe(viewLifecycleOwner, {
            when (it) {
                TVShowViewModel.LoadingState.LOADING -> displayProgressbar()
                TVShowViewModel.LoadingState.SUCCESS -> displayTVList()
                TVShowViewModel.LoadingState.ERROR -> displayErrorMessage()
                else -> displayErrorMessage()
            }
        })
    }

    private fun displayTVList() {
        binding.tvDetailsErrorMessage.visibility = View.GONE
        binding.tvDetailsProgressBar.visibility = View.GONE
    }

    private fun displayErrorMessage() {
        binding.tvDetailsErrorMessage.visibility = View.VISIBLE
        binding.tvDetailsProgressBar.visibility = View.GONE
    }

    private fun displayProgressbar() {
        binding.tvDetailsProgressBar.visibility = View.VISIBLE
        binding.tvDetailsErrorMessage.visibility = View.GONE
    }
}