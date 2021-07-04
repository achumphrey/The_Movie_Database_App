package com.example.themoviedatabaseapp.adapter.favourites

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.model.today.Result
import com.example.themoviedatabaseapp.utils.SharedPreference
import com.squareup.picasso.Picasso

class TodFavViewholder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val favTdImage: ImageView = itemView.findViewById(R.id.tdFavShowImage)
    private val favTdName: TextView = itemView.findViewById(R.id.tdFavShowName)
    private val favTdFirstAirDate: TextView = itemView.findViewById(R.id.tdFavDateFirstAired)
    private val favTdRating: TextView = itemView.findViewById(R.id.tdFavshowRating)
    private val favTdFavImageBtn: ImageView = itemView.findViewById(R.id.tdTvFavImageBtn)

    fun bindItem(tvTdItem: Result, listener: TodFavListener) {

        val httpPrefix = "https://www.themoviedb.org/t/p/w220_and_h330_face"
        favTdName.text = tvTdItem.name
        favTdRating.text = tvTdItem.voteAverage.toString()
        favTdFirstAirDate.text = tvTdItem.firstAirDate
        Picasso.get()
            .load(httpPrefix + tvTdItem.posterPath)
            .error(R.drawable.ic_launcher_background)
            .into(favTdImage)

        itemView.setOnClickListener {
            listener.tdFavItemClickListener(tvTdItem)
        }

        if (SharedPreference().checkIfInSharePref(tvTdItem))
            favTdFavImageBtn.setImageResource(R.drawable.solidredheart)

        favTdFavImageBtn.setOnClickListener {
            if (SharedPreference().checkIfInSharePref(tvTdItem)) {
                favTdFavImageBtn.setImageResource(R.drawable.heartgrey)
                SharedPreference().removeFavorite(tvTdItem)
            } else {
                favTdFavImageBtn.setImageResource(R.drawable.solidredheart)
                SharedPreference().addFavorite(tvTdItem)
            }
        }
    }
}