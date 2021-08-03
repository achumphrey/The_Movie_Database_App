package com.example.themoviedatabaseapp.adapter.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.databinding.TdFavItemListBinding
import com.example.themoviedatabaseapp.model.today.TdResult

class TodFavAdapter(
    private val tdFavList: MutableList<TdResult>,
    private val listener: TodFavListener
) : RecyclerView.Adapter<TodFavViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            TodFavViewholder {

        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: TdFavItemListBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.td_fav_item_list,
            parent,
            false
        )

        return TodFavViewholder(binding)
    }

    override fun onBindViewHolder(holder: TodFavViewholder, position: Int) {
        holder.bindItem(tdFavList[position], listener)
    }

    override fun getItemCount(): Int {
        return tdFavList.size
    }

    fun updateTdFavList(newTodFavList: List<TdResult>) {
        tdFavList.clear()
        tdFavList.addAll(newTodFavList)
        notifyDataSetChanged()
    }
}