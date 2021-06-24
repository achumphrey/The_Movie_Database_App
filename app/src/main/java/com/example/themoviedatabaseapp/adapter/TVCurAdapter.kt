package com.example.themoviedatabaseapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.model.current.Result

class TVCurAdapter(
    private val tvCurItem: MutableList<Result>,
    private val listener: TVCurListener
) : RecyclerView.Adapter<TVCurViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVCurViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.tv_cur_show_item_list,
                parent,
                false
            )

        return TVCurViewHolder(view)
    }

    override fun onBindViewHolder(holder: TVCurViewHolder, position: Int) {
        holder.bindItem(tvCurItem[position], listener)
    }

    override fun getItemCount(): Int {
        return tvCurItem.size
    }

    fun updateTvCurList(newTvCurList: List<Result>) {
        tvCurItem.clear()
        tvCurItem.addAll(newTvCurList)
        notifyDataSetChanged()
    }
}