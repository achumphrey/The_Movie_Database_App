package com.example.themoviedatabaseapp.adapter.favourites

import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.databinding.TdFavItemListBinding
import com.example.themoviedatabaseapp.model.today.Result
import com.example.themoviedatabaseapp.utils.SharedPreference
import com.squareup.picasso.Picasso

class TodFavViewholder(private val binding: TdFavItemListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindItem(tvTdItem: Result, listener: TodFavListener) {

        val httpPrefix = "https://www.themoviedb.org/t/p/w220_and_h330_face"
        binding.tdFavShowName.text = tvTdItem.name
        binding.tdFavshowRating.text = tvTdItem.voteAverage.toString()
        binding.tdFavDateFirstAired.text = tvTdItem.firstAirDate

        Picasso.get()
            .load(httpPrefix + tvTdItem.posterPath)
            .error(R.drawable.ic_launcher_background)
            .into(binding.tdFavShowImage)

        itemView.setOnClickListener {
            listener.tdFavItemClickListener(tvTdItem)
        }

        if (SharedPreference().checkIfInSharePref(tvTdItem))
            binding.tdTvFavImageBtn.setImageResource(R.drawable.solidredheart)

        binding.tdTvFavImageBtn.setOnClickListener {
            if (SharedPreference().checkIfInSharePref(tvTdItem)) {
                binding.tdTvFavImageBtn.setImageResource(R.drawable.heartgrey)
                SharedPreference().removeFavorite(tvTdItem)
            } else {
                binding.tdTvFavImageBtn.setImageResource(R.drawable.solidredheart)
                SharedPreference().addFavorite(tvTdItem)
            }
        }
    }
}