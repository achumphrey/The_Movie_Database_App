package com.example.themoviedatabaseapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.themoviedatabaseapp.di.TVShowApp
import com.example.themoviedatabaseapp.fragments.TVCurrentlyAiring.Companion.INTENT_MESSAGE
import com.example.themoviedatabaseapp.model.TVDetails.TVShowDetails
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModel
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModelFactory
import com.squareup.picasso.Picasso
import javax.inject.Inject

class TVDetailsActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_t_v_details)

        TVShowApp.getTVShowComponent().inject(this)

        initializeViews()

        val id: Int = intent.getIntExtra(INTENT_MESSAGE, 0)

    /*    tvShowViewModel = ViewModelProvider(
            this,
            tvShowViewModelFactory)
            .get(TVShowViewModel::class.java)
*/
        tvShowViewModel = ViewModelProvider(
            viewModelStore,
            tvShowViewModelFactory)
            .get(TVShowViewModel::class.java)

        tvShowViewModel.fetchTVDetails(id)

        tvShowViewModel.tvDetails().observe(this, {
            Log.i("Details Object: ", it.name)
            populateView(it)
        })

        tvShowViewModel.errorMessage().observe(this, {
            tvDetailsErrorMessage.text = it
        })

        tvShowViewModel.loadingState.observe(this, {
            when (it) {
                TVShowViewModel.LoadingState.LOADING -> displayProgressbar()
                TVShowViewModel.LoadingState.SUCCESS -> displayTVList()
                TVShowViewModel.LoadingState.ERROR -> displayErrorMessage()
                else -> displayErrorMessage()
            }
        })
    }

    private fun initializeViews() {
        detailImage = findViewById(R.id.detailImageView)
        detailName = findViewById(R.id.detailName)
        detailLang = findViewById(R.id.detailLang)
        detailGenre = findViewById(R.id.detailGenre)
        detailFirstAirDate = findViewById(R.id.detailFirstAirDate)
        detailOverview = findViewById(R.id.detailOverview)
        detailProCountry = findViewById(R.id.detailProdCountry)
        detailLastAirDate = findViewById(R.id.detailLastAirDate)
        detailLastEpisodeToAir = findViewById(R.id.detailLastEpiToAir)
        tvDetailsErrorMessage = findViewById(R.id.tvDetailsErrorMessage)
        tvDetailsProgressBar = findViewById(R.id.tvDetailsProgressBar)
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
}