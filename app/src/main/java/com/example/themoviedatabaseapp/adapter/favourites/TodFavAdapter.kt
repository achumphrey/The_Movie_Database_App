package com.example.themoviedatabaseapp.adapter.favourites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.model.today.Result

class TodFavAdapter(
    private val tdFavList: MutableList<Result>,
    private val listener: TodFavListener
) : RecyclerView.Adapter<TodFavViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodFavViewholder {

        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.td_fav_item_list,
                parent,
                false
            )

        return TodFavViewholder(view)
    }

    override fun onBindViewHolder(holder: TodFavViewholder, position: Int) {
        holder.bindItem(tdFavList[position], listener)
    }

    override fun getItemCount(): Int {
        return tdFavList.size
    }

    fun updateTdFavList(newTodFavList: List<Result>) {
        tdFavList.clear()
        tdFavList.addAll(newTodFavList)
        notifyDataSetChanged()
    }
}