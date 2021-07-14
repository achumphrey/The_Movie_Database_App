package com.example.themoviedatabaseapp.adapter.favourites

import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.databinding.CurFavItemListBinding
import com.example.themoviedatabaseapp.model.current.Result
import com.example.themoviedatabaseapp.utils.CurSharedPreference
import com.squareup.picasso.Picasso

class CurFavViewHolder(private val binding: CurFavItemListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindItem(tvCurItem: Result, listener: CurFavListener) {

        val httpPrefix = "https://www.themoviedb.org/t/p/w220_and_h330_face"
        binding.curFavshowName.text = tvCurItem.name
        binding.curFavShowRating.text = tvCurItem.voteAverage.toString()
        binding.curFavDateFirstAired.text = tvCurItem.firstAirDate
        Picasso.get()
            .load(httpPrefix + tvCurItem.posterPath)
            .error(R.drawable.ic_launcher_background)
            .into(binding.curFavShowImage)

        itemView.setOnClickListener {
            listener.curFavItemClickListener(tvCurItem)
        }

        if (CurSharedPreference().checkIfInSharePref(tvCurItem))
            binding.curFavImageBtn.setImageResource(R.drawable.solidredheart)

        binding.curFavImageBtn.setOnClickListener {
            if (CurSharedPreference().checkIfInSharePref(tvCurItem)) {
                binding.curFavImageBtn.setImageResource(R.drawable.heartgrey)
                CurSharedPreference().removeFavorite(tvCurItem)
            } else {
                binding.curFavImageBtn.setImageResource(R.drawable.solidredheart)
                CurSharedPreference().addFavorite(tvCurItem)
            }
        }
    }
}