package com.example.themoviedatabaseapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.model.today.Result

class TVTodayAdapter(
    private val tdTVShowList: MutableList<Result>,
    private val listener: TVTodayListener
) : RecyclerView.Adapter<TVTodayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVTodayViewHolder {

        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.tv_td_show_item_list,
                parent,
                false
            )

        return TVTodayViewHolder(view)
    }

    override fun onBindViewHolder(holder: TVTodayViewHolder, position: Int) {
        holder.bindItem(tdTVShowList[position], listener)
    }

    override fun getItemCount(): Int {
        return tdTVShowList.size
    }

    fun updateTvTdList(newTvTodayList: List<Result>) {
        tdTVShowList.clear()
        tdTVShowList.addAll(newTvTodayList)
        notifyDataSetChanged()
    }
}