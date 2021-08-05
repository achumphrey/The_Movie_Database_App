package com.example.themoviedatabaseapp.adapter

import  android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.databinding.TvTdShowItemListBinding
import com.example.themoviedatabaseapp.model.today.TdResult
import kotlin.collections.ArrayList

@Suppress("UNCHECKED_CAST")
class TVTodayAdapter(
    private val tdTVShowList: MutableList<TdResult>,
    private val listener: TVTodayListener
) : RecyclerView.Adapter<TVTodayViewHolder>(), Filterable {

    private var filteredList: ArrayList<TdResult> = tdTVShowList as ArrayList<TdResult>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVTodayViewHolder {

        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: TvTdShowItemListBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.tv_td_show_item_list,
            parent,
            false)

        return TVTodayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TVTodayViewHolder, position: Int) {
        holder.bindItem(filteredList[position], listener)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun updateTvTdList(newTvTodayList: List<TdResult>) {
        filteredList.clear()
        filteredList.addAll(newTvTodayList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return (object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString: String = constraint.toString()

                if (charString.isEmpty()) {
                    filteredList = tdTVShowList as ArrayList<TdResult>
                } else {
                    val newList: ArrayList<TdResult> = ArrayList()
                    tdTVShowList.forEach { tvShow: TdResult ->
                        if (
                            tvShow.name.contains(charString, true)
                            || tvShow.firstAirDate.contains(charString)
                            || tvShow.voteAverage.toString().contains(charString)
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

                filteredList = if(results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<TdResult>

                //    filteredList = results?.values as ArrayList<TdResult>
                notifyDataSetChanged()
            }
        })
    }
}