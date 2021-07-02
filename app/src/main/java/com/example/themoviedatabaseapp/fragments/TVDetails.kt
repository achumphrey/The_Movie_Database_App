package com.example.themoviedatabaseapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.di.TVShowApp
import com.example.themoviedatabaseapp.model.TVDetails.TVShowDetails
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModel
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModelFactory
import com.squareup.picasso.Picasso
import javax.inject.Inject

class TVDetails : Fragment() {

    private lateinit var detailImage: ImageView
    private lateinit var detailName: TextView
    private lateinit var detailLang: TextView
    private lateinit var detailGenre: TextView
    private lateinit var detailFirstAirDate: TextView
    private lateinit var detailOverview: TextView
    private lateinit var detailProCountry: TextView
    private lateinit var detailLastAirDate: TextView
    private lateinit var detailLastEpisodeToAir: TextView
    private lateinit var tvDetailsErrorMessage: TextView
    private lateinit var tvDetailsProgressBar: ProgressBar

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
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_t_v_details,
            container,
            false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()

        tvShowViewModel.tvDetails().observe(viewLifecycleOwner, {
            populateView(it)
        })

        tvShowViewModel.errorMessage().observe(viewLifecycleOwner, {
            tvDetailsErrorMessage.text = it
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

    private fun initializeViews() {
        detailImage = requireView().findViewById(R.id.detailImageView)
        detailName = requireView().findViewById(R.id.detailName)
        detailLang = requireView().findViewById(R.id.detailLang)
        detailGenre = requireView().findViewById(R.id.detailGenre)
        detailFirstAirDate = requireView().findViewById(R.id.detailFirstAirDate)
        detailOverview = requireView().findViewById(R.id.detailOverview)
        detailProCountry = requireView().findViewById(R.id.detailProdCountry)
        detailLastAirDate = requireView().findViewById(R.id.detailLastAirDate)
        detailLastEpisodeToAir = requireView().findViewById(R.id.detailLastEpiToAir)
        tvDetailsErrorMessage = requireView().findViewById(R.id.tvDetailsErrorMessage)
        tvDetailsProgressBar = requireView().findViewById(R.id.tvDetailsProgressBar)
    }

    private fun populateView(item: TVShowDetails) {
        detailName.text = String.format("Name: ${item.name}")
        detailLang.text = String.format("Language: ${item.originalLanguage}")
        detailGenre.text = String.format("Genre: ${item.genres[0].name}")
        detailFirstAirDate.text = String.format("First Air Date: ${item.firstAirDate}")
        detailOverview.text = String.format("Overview:\n${item.overview}")
        detailLastAirDate.text = String.format("Last Air Date: ${item.lastAirDate}")
        detailLastEpisodeToAir.text =
            String.format("Last Episode To Air: ${item.lastEpisodeToAir.name}")

        if (item.productionCountries.isEmpty()) {
            detailProCountry.text = String.format("Production Country: No Data")
        } else {
            detailProCountry.text =
                String.format("Production Country: ${item.productionCountries.get(0).name}")
        }

        val httpPrefix = "https://www.themoviedb.org/t/p/w220_and_h330_face"
        Picasso.get()
            .load(httpPrefix + item.posterPath)
            .error(R.drawable.ic_launcher_background)
            .into(detailImage)
    }

    private fun displayTVList() {
        tvDetailsErrorMessage.visibility = View.GONE
        tvDetailsProgressBar.visibility = View.GONE
    }

    private fun displayErrorMessage() {
        tvDetailsErrorMessage.visibility = View.VISIBLE
        tvDetailsProgressBar.visibility = View.GONE
    }

    private fun displayProgressbar() {
        tvDetailsProgressBar.visibility = View.VISIBLE
        tvDetailsErrorMessage.visibility = View.GONE
    }

    /*
    val args: ConfirmationFragmentArgs by navArgs()
    val tv: TextView = view.findViewById(R.id.textViewAmount)
    val amount = args.amount
    tv.text = amount.toString()

     */
}