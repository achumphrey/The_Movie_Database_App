package com.example.themoviedatabaseapp.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
    private var tvId: Int = 0

    @Inject
    lateinit var tvShowViewModelFactory: TVShowViewModelFactory
    private lateinit var tvShowViewModel: TVShowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        TVShowApp.getTVShowComponent().inject(this)

        val args: TVDetailsArgs by navArgs()
        tvId = args.itemID

        tvShowViewModel = ViewModelProvider(
            this,
            tvShowViewModelFactory
        )
            .get(TVShowViewModel::class.java)

        tvShowViewModel.getCount(tvId)
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

        tvShowViewModel.tvDetails().observe(viewLifecycleOwner, { tvObject ->
            binding.tvDetailObject = tvObject
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

        val toolbar: Toolbar = requireView().findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dmenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.dtmenu -> {
                tvShowViewModel.delShowFromDB(tvId)
                requireActivity()
                    .supportFragmentManager
                    .popBackStackImmediate()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val menuItem: MenuItem = menu.findItem(R.id.tvFav)
        val searchMenu: MenuItem = menu.findItem(R.id.search)
        searchMenu.isVisible = false
        menuItem.isVisible = false
    }
}