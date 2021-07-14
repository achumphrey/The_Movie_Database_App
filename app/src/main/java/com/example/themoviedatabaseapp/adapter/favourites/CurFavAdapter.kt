package com.example.themoviedatabaseapp.adapter.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.databinding.CurFavItemListBinding
import com.example.themoviedatabaseapp.model.current.Result

class CurFavAdapter(
    private val curFavItem: MutableList<Result>,
    private val listener: CurFavListener
) : RecyclerView.Adapter<CurFavViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            CurFavViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: CurFavItemListBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.cur_fav_item_list,
            parent,
            false)

        return CurFavViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurFavViewHolder, position: Int) {
        holder.bindItem(curFavItem[position], listener)
    }

    override fun getItemCount(): Int {
        return curFavItem.size
    }

    fun updateCurFavList(newcurFavList: List<Result>) {
        curFavItem.clear()
        curFavItem.addAll(newcurFavList)
        notifyDataSetChanged()
    }
}