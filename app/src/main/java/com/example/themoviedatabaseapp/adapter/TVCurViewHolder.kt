package com.example.themoviedatabaseapp.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.databinding.TvCurShowItemListBinding
import com.example.themoviedatabaseapp.model.current.Result
import com.example.themoviedatabaseapp.utils.CurSharedPreference
import com.squareup.picasso.Picasso

class TVCurViewHolder(private val binding: TvCurShowItemListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindItem(tvCurItem: Result, listener: TVCurListener) {

        val httpPrefix = "https://www.themoviedb.org/t/p/w220_and_h330_face"
        binding.tvshowName.text = tvCurItem.name
        binding.tvshowRating.text = tvCurItem.voteAverage.toString()
        binding.dateFirstAired.text = tvCurItem.firstAirDate
        Picasso.get()
            .load(httpPrefix + tvCurItem.posterPath)
            .error(R.drawable.ic_launcher_background)
            .into(binding.tvshowImage)

        itemView.setOnClickListener {
            listener.tvCurItemClickListener(tvCurItem)
        }

        if (CurSharedPreference().checkIfInSharePref(tvCurItem))
            binding.tvShowFavImage.setImageResource(R.drawable.solidredheart)

        binding.tvShowFavImage.setOnClickListener {
            if (CurSharedPreference().checkIfInSharePref(tvCurItem)) {
                binding.tvShowFavImage.setImageResource(R.drawable.heartgrey)
                CurSharedPreference().removeFavorite(tvCurItem)
            } else {
                binding.tvShowFavImage.setImageResource(R.drawable.solidredheart)
                CurSharedPreference().addFavorite(tvCurItem)
            }
        }
    }
}