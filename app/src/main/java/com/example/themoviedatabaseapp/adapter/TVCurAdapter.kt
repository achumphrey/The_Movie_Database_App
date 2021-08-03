package com.example.themoviedatabaseapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.databinding.TvCurShowItemListBinding
import com.example.themoviedatabaseapp.model.current.CurResult
import java.util.*
import kotlin.collections.ArrayList

@Suppress("UNCHECKED_CAST")
class TVCurAdapter(
    private val tvCurItem: MutableList<CurResult>,
    private val listener: TVCurListener
) : RecyclerView.Adapter<TVCurViewHolder>(), Filterable {

    private var filteredList: ArrayList<CurResult> = tvCurItem as ArrayList<CurResult>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVCurViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: TvCurShowItemListBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.tv_cur_show_item_list,
            parent,
            false
        )

        return TVCurViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TVCurViewHolder, position: Int) {
        holder.bindItem(filteredList[position], listener)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun updateTvCurList(newTvCurList: List<CurResult>) {
        filteredList.clear()
        filteredList.addAll(newTvCurList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return (object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString: String = constraint.toString()

                if (charString.isEmpty()) {
                    filteredList = tvCurItem as ArrayList<CurResult>
                } else {
                    val newList: ArrayList<CurResult> = ArrayList()
                    tvCurItem.forEach { tvShow: CurResult ->
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
                filteredList = results?.values as ArrayList<CurResult>
                notifyDataSetChanged()
            }
        })
    }
}