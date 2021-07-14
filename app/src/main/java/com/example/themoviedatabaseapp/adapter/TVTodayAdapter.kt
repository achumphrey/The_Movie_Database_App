package com.example.themoviedatabaseapp.adapter

import  android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.model.today.Result
import java.util.*
import kotlin.collections.ArrayList

@Suppress("UNCHECKED_CAST")
class TVTodayAdapter(
    private val tdTVShowList: MutableList<Result>,
    private val listener: TVTodayListener
) : RecyclerView.Adapter<TVTodayViewHolder>(), Filterable {

    private var filteredList: ArrayList<Result> = tdTVShowList as ArrayList<Result>

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
        holder.bindItem(filteredList[position], listener)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun updateTvTdList(newTvTodayList: List<Result>) {
        filteredList.clear()
        filteredList.addAll(newTvTodayList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return (object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString: String = constraint.toString()

                if (charString.isEmpty()) {
                    filteredList = tdTVShowList as ArrayList<Result>
                } else {
                    val newList: ArrayList<Result> = ArrayList()
                    tdTVShowList.forEach { tvShow: Result ->
                        if (
                            tvShow.name.contains(charString)
                            || tvShow.firstAirDate.lowercase(Locale.ROOT).contains(charString)
                            || tvShow.voteAverage.toString().lowercase(Locale.ROOT)
                                .contains(charString)
                        ) {
                            newList.add(tvShow)
                        }
                    }

                    filteredList = newList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<Result>
                notifyDataSetChanged()
            }
        })
    }
}