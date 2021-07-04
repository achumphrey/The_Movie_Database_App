package com.example.themoviedatabaseapp.adapter.favourites

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.model.current.Result
import com.example.themoviedatabaseapp.utils.CurSharedPreference
import com.squareup.picasso.Picasso

class CurFavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val favCurImage: ImageView = itemView.findViewById(R.id.curFavShowImage)
    private val favCurName: TextView = itemView.findViewById(R.id.curFavshowName)
    private val favCurFirstAirDate: TextView = itemView.findViewById(R.id.curFavDateFirstAired)
    private val favCurRating: TextView = itemView.findViewById(R.id.curFavShowRating)
    private val favCurImageBtn: ImageView = itemView.findViewById(R.id.curFavImageBtn)

    fun bindItem(tvCurItem: Result, listener: CurFavListener) {

        val httpPrefix = "https://www.themoviedb.org/t/p/w220_and_h330_face"
        favCurName.text = tvCurItem.name
        favCurRating.text = tvCurItem.voteAverage.toString()
        favCurFirstAirDate.text = tvCurItem.firstAirDate
        Picasso.get()
            .load(httpPrefix + tvCurItem.posterPath)
            .error(R.drawable.ic_launcher_background)
            .into(favCurImage)

        itemView.setOnClickListener {
            listener.curFavItemClickListener(tvCurItem)
        }

        if (CurSharedPreference().checkIfInSharePref(tvCurItem))
            favCurImageBtn.setImageResource(R.drawable.solidredheart)

        favCurImageBtn.setOnClickListener {
            if (CurSharedPreference().checkIfInSharePref(tvCurItem)) {
                favCurImageBtn.setImageResource(R.drawable.heartgrey)
                CurSharedPreference().removeFavorite(tvCurItem)
            } else {
                favCurImageBtn.setImageResource(R.drawable.solidredheart)
                CurSharedPreference().addFavorite(tvCurItem)
            }
        }
    }
}