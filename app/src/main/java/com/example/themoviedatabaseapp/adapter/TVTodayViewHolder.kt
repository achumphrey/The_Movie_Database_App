package com.example.themoviedatabaseapp.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.model.today.Result
import com.example.themoviedatabaseapp.utils.SharedPreference
import com.squareup.picasso.Picasso

class TVTodayViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val tvTdImage: ImageView = itemView.findViewById(R.id.tdTvshowImage)
    private val tvTdName: TextView = itemView.findViewById(R.id.tdTvshowName)
    private val tvTdFirstAirDate: TextView = itemView.findViewById(R.id.tdDateFirstAired)
    private val tvTdRating: TextView = itemView.findViewById(R.id.tdTvshowRating)
    private val tvTdFavImage: ImageView = itemView.findViewById(R.id.tdTvFavImage)

    fun bindItem(tvTdItem: Result, listener: TVTodayListener ){

        val httpPrefix = "https://www.themoviedb.org/t/p/w220_and_h330_face"
        tvTdName.text = tvTdItem.name
        tvTdRating.text = tvTdItem.voteAverage.toString()
        tvTdFirstAirDate.text = tvTdItem.firstAirDate
        Picasso.get()
            .load(httpPrefix + tvTdItem.posterPath)
            .error(R.drawable.ic_launcher_background)
            .into(tvTdImage)

        itemView.setOnClickListener{
            listener.TVTodItemClickListener(tvTdItem)
        }

        if(SharedPreference().checkIfInSharePref(tvTdItem))
            tvTdFavImage.setImageResource(R.drawable.heartsolid)

        tvTdFavImage.setOnClickListener{
            if(SharedPreference().checkIfInSharePref(tvTdItem)){
                tvTdFavImage.setImageResource(R.drawable.heartgrey)
                SharedPreference().removeFavorite(tvTdItem)
            }else{
                tvTdFavImage.setImageResource(R.drawable.heartsolid)
                SharedPreference().addFavorite(tvTdItem)
            }
        }
    }


}