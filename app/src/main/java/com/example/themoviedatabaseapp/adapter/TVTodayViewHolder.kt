package com.example.themoviedatabaseapp.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.databinding.TvTdShowItemListBinding
import com.example.themoviedatabaseapp.model.today.TdResult
import com.example.themoviedatabaseapp.utils.SharedPreference
import com.squareup.picasso.Picasso

class TVTodayViewHolder(private val binding: TvTdShowItemListBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(tvTdItem: TdResult, listener: TVTodayListener ){

        val httpPrefix = "https://www.themoviedb.org/t/p/w220_and_h330_face"
        binding.tdTvshowName.text = tvTdItem.name
        binding.tdTvshowRating.text = tvTdItem.voteAverage.toString()
        binding.tdDateFirstAired.text = tvTdItem.firstAirDate

        Picasso.get()
            .load(httpPrefix + tvTdItem.posterPath)
            .error(R.drawable.ic_launcher_background)
            .into(binding.tdTvshowImage)

        itemView.setOnClickListener{
            listener.TVTodItemClickListener(tvTdItem)
        }

        if(SharedPreference().checkIfInSharePref(tvTdItem))
            binding.tdTvFavImage.setImageResource(R.drawable.solidredheart)

        binding.tdTvFavImage.setOnClickListener{
            if(SharedPreference().checkIfInSharePref(tvTdItem)){
                binding.tdTvFavImage.setImageResource(R.drawable.heartgrey)
                SharedPreference().removeFavorite(tvTdItem)
            }else{
                binding.tdTvFavImage.setImageResource(R.drawable.solidredheart)
                SharedPreference().addFavorite(tvTdItem)
            }
        }
    }
}