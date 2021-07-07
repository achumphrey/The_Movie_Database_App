package com.example.themoviedatabaseapp.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.themoviedatabaseapp.fragments.FirstScreen
import com.example.themoviedatabaseapp.fragments.TVAiringToday
import com.example.themoviedatabaseapp.fragments.TVCurrentlyAiring

class ViewPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle)
    : FragmentStateAdapter(
    fm, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> FirstScreen()
            1 -> TVAiringToday()
            2 -> TVCurrentlyAiring()
            else -> FirstScreen()
        }
    }
}