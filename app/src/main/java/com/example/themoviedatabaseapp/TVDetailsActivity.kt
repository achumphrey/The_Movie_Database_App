package com.example.themoviedatabaseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.themoviedatabaseapp.fragments.TVCurrentlyAiring.Companion.INTENT_MESSAGE
import com.example.themoviedatabaseapp.model.TVDetails.TodayTVShowDetails
import com.example.themoviedatabaseapp.remote.WebClient
import com.example.themoviedatabaseapp.remote.WebServices
import com.example.themoviedatabaseapp.repository.TVRepo
import com.example.themoviedatabaseapp.repository.TVRepoImpl
import com.example.themoviedatabaseapp.viewmodel.TVViewModel
import com.example.themoviedatabaseapp.viewmodel.ViewModelFactory
import com.squareup.picasso.Picasso

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

    private lateinit var repo: TVRepo
    private lateinit var webService: WebServices
    private lateinit var tvViewModel: TVViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_t_v_details)

        initializeViews()

        val id: Int = intent.getIntExtra(INTENT_MESSAGE, 0)
        Log.i("Passed ID", id.toString())

        webService = WebClient().retrofitInstance
        repo = TVRepoImpl(webService)

        tvViewModel = ViewModelProvider(
            this,
            ViewModelFactory(repo)
        ).get(TVViewModel::class.java)

        tvViewModel.fetchTVDetails(id)

        tvViewModel.tvDetails().observe(this, {

            Log.i("TV Details:", it.name)

            val httpPrefix = "https://www.themoviedb.org/t/p/w220_and_h330_face"
            Picasso.get()
                .load(httpPrefix + it.posterPath)
                .error(R.drawable.ic_launcher_background)
                .into(detailImage)

            populateView(it)

        })
    }

    private fun initializeViews(){
        detailImage = findViewById(R.id.detailImageView)
        detailName = findViewById(R.id.detailName)
        detailLang = findViewById(R.id.detailLang)
        detailGenre = findViewById(R.id.detailGenre)
        detailFirstAirDate = findViewById(R.id.detailFirstAirDate)
        detailOverview = findViewById(R.id.detailOverview)
        detailProCountry = findViewById(R.id.detailProdCountry)
        detailLastAirDate = findViewById(R.id.detailLastAirDate)
        detailLastEpisodeToAir = findViewById(R.id.detailLastEpiToAir)
    }

    private fun populateView(item: TodayTVShowDetails){
        detailName.text = String.format("Name: ${item.name}")
        detailLang.text = String.format("Language: ${item.originalLanguage}")
        detailGenre.text = String.format("Genre: ${item.genres[0].name}")
        detailFirstAirDate.text = String.format("First Air Date: ${item.firstAirDate}")
        detailOverview.text = String.format("Overview:\n${item.overview}")
        detailLastAirDate.text = String.format("Last Air Date: ${item.lastAirDate}")
        detailLastEpisodeToAir.text = String.format("Last Episode To Air: ${item.lastEpisodeToAir.name}")

        if(item.productionCountries.isEmpty()){
            detailProCountry.text = String.format("Production Country: No Data")
        }else{
            detailProCountry.text = String.format("Production Country: ${item.productionCountries.get(0).name}")
        }
    }
}