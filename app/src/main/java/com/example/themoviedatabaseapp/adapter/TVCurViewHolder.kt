package com.example.themoviedatabaseapp.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.model.current.Result
import com.example.themoviedatabaseapp.utils.CurSharedPreference
import com.squareup.picasso.Picasso

class TVCurViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val tvCurImage: ImageView = itemView.findViewById(R.id.tvshowImage)
    private val tvCurName: TextView = itemView.findViewById(R.id.tvshowName)
    private val tvCurFirstAirDate: TextView = itemView.findViewById(R.id.dateFirstAired)
    private val tvCurRating: TextView = itemView.findViewById(R.id.tvshowRating)
    private val tvCurFavImage: ImageView = itemView.findViewById(R.id.tvShowFavImage)

    fun bindItem(tvCurItem: Result, listener: TVCurListener ){

        val httpPrefix = "https://www.themoviedb.org/t/p/w220_and_h330_face"
        tvCurName.text = tvCurItem.name
        tvCurRating.text = tvCurItem.voteAverage.toString()
        tvCurFirstAirDate.text = tvCurItem.firstAirDate
        Picasso.get()
            .load(httpPrefix + tvCurItem.posterPath)
            .error(R.drawable.ic_launcher_background)
            .into(tvCurImage)

        itemView.setOnClickListener{
            listener.tvCurItemClickListener(tvCurItem)
        }

        if(CurSharedPreference().checkIfInSharePref(tvCurItem))
            tvCurFavImage.setImageResource (R.drawable.solidredheart)

        tvCurFavImage.setOnClickListener{
            if(CurSharedPreference().checkIfInSharePref(tvCurItem)){
                tvCurFavImage.setImageResource(R.drawable.heartgrey)
                CurSharedPreference().removeFavorite(tvCurItem)
            }else{
                tvCurFavImage.setImageResource(R.drawable.solidredheart)
                CurSharedPreference().addFavorite(tvCurItem)
            }
        }

    }
}