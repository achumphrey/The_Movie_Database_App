package com.example.themoviedatabaseapp.fragments.favourites

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.adapter.favourites.CurFavAdapter
import com.example.themoviedatabaseapp.adapter.favourites.CurFavListener
import com.example.themoviedatabaseapp.model.current.Result
import com.example.themoviedatabaseapp.utils.CurSharedPreference

class CurFavFragment : Fragment() {

    private lateinit var curFavRecyclerView: RecyclerView
    private lateinit var curFavAdapter: CurFavAdapter
    private lateinit var errorMessage: TextView
    private lateinit var progbar: ProgressBar
    private lateinit var mMenu: Menu
    var curDataArrayList: ArrayList<Result> = arrayListOf()

    private val tvCurFavClickListener: CurFavListener = object : CurFavListener {
        override fun curFavItemClickListener(itemList: Result) {
            callDetailsFragment(itemList.id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        curDataArrayList = CurSharedPreference().getFavorites()
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_cur_fav,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        curFavRecyclerView = requireView().findViewById(R.id.recyViewFavCurAiring)
        errorMessage = requireView().findViewById(R.id.tvFavCurErrorMessage)
        progbar = requireView().findViewById(R.id.tvFavCurProgressBar)

        if (curDataArrayList.isNullOrEmpty()) {
            curFavRecyclerView.visibility = View.GONE
            errorMessage.text = String.format("No Favourites to display")
            errorMessage.visibility = View.VISIBLE
            progbar.visibility = View.GONE
        } else {
            curFavRecyclerView.visibility = View.VISIBLE
            progbar.visibility = View.GONE
            errorMessage.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        curFavRecyclerView.layoutManager = LinearLayoutManager(context)
        curFavAdapter = CurFavAdapter(mutableListOf(), tvCurFavClickListener)
        curFavRecyclerView.adapter = curFavAdapter
        curFavAdapter.updateCurFavList(curDataArrayList)
    }

    private fun callDetailsFragment(id: Int) {
        val directions = CurFavFragmentDirections.actionCurFavFragmentToTVDetails(id)
        findNavController().navigate(directions)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        mMenu = menu
        if (curDataArrayList.isNullOrEmpty()) {
            mMenu.findItem(R.id.tvcurFav).setIcon(R.drawable.heartgrey)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tvcurFav -> {
                CurSharedPreference().deleteAllFavorites()
                mMenu.findItem(R.id.tvcurFav).setIcon(R.drawable.heartgrey)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}