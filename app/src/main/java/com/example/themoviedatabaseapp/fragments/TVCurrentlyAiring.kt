package com.example.themoviedatabaseapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.adapter.TVCurAdapter
import com.example.themoviedatabaseapp.adapter.TVCurListener
import com.example.themoviedatabaseapp.di.TVShowApp
import com.example.themoviedatabaseapp.model.current.CurResult
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModel
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModelFactory
import javax.inject.Inject


@Suppress("DEPRECATION")
class TVCurrentlyAiring : Fragment() {

    private lateinit var tvCurRecyclerView: RecyclerView
    private lateinit var tvProgressBar: ProgressBar
    private lateinit var tvErrorMessage: TextView
    private lateinit var tvSearch: SearchView

    @Inject
    lateinit var tvShowViewModelFactory: TVShowViewModelFactory
    private lateinit var tvShowViewModel: TVShowViewModel
    private lateinit var tvCurAdapter: TVCurAdapter

    private val tvCurClickListener: TVCurListener = object : TVCurListener {
        override fun tvCurItemClickListener(itemList: CurResult) {
            callDetailsFragment(itemList.id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        TVShowApp.getTVShowComponent().inject(this)

        tvShowViewModel =
            ViewModelProvider(this, tvShowViewModelFactory)
                .get(TVShowViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        tvShowViewModel.tvCurrentFromViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_t_v__currently__airing,
            container,
            false
        )
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvSearch = requireView().findViewById(R.id.tv_Cur_search)
        tvCurRecyclerView = requireView().findViewById(R.id.recyViewTvCurAiring)
        tvErrorMessage = requireView().findViewById(R.id.tvErrorMessage)
        tvProgressBar = requireView().findViewById(R.id.tvProgressBar)

        val searchIcon = tvSearch.findViewById<ImageView>(R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.WHITE)

        val cancelIcon = tvSearch.findViewById<ImageView>(R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.WHITE)

        val textView = tvSearch.findViewById<TextView>(R.id.search_src_text)
        textView.setTextColor(Color.WHITE)
        // If you want to change the color of the cursor, change the 'colorAccent' in colors.xml

        tvShowViewModel.loadingState.observe(viewLifecycleOwner, {
            when (it) {
                TVShowViewModel.LoadingState.LOADING -> displayProgressbar()
                TVShowViewModel.LoadingState.SUCCESS -> displayTVList()
                TVShowViewModel.LoadingState.ERROR -> displayErrorMessage()
                else -> displayErrorMessage()
            }
        })

        tvShowViewModel.curTVLiveData().observe(viewLifecycleOwner, {
            tvCurAdapter.updateTvCurList(it)
        })

        tvShowViewModel.errorMessage().observe(viewLifecycleOwner, {
            tvErrorMessage.text = it
        })

        getSearch()
    }

    private fun setupRecyclerView() {
        tvCurRecyclerView.layoutManager = LinearLayoutManager(context)
        tvCurAdapter = TVCurAdapter(mutableListOf(), tvCurClickListener)
        tvCurRecyclerView.adapter = tvCurAdapter
    }

    private fun displayTVList() {
        tvErrorMessage.visibility = View.GONE
        tvCurRecyclerView.visibility = View.VISIBLE
        tvProgressBar.visibility = View.GONE
    }

    private fun displayErrorMessage() {
        tvErrorMessage.visibility = View.VISIBLE
        tvCurRecyclerView.visibility = View.GONE
        tvProgressBar.visibility = View.GONE
    }

    private fun displayProgressbar() {
        tvProgressBar.visibility = View.VISIBLE
        tvCurRecyclerView.visibility = View.GONE
        tvErrorMessage.visibility = View.GONE
    }

    private fun callDetailsFragment(id: Int) {
        val directions = TVCurrentlyAiringDirections.actionTVCurrentlyAiringToTVDetails(id)
        findNavController().navigate(directions)
    }

    private fun callToCurFavFragment() {
        val directions = TVCurrentlyAiringDirections.actionTVCurrentlyAiringToCurFavFragment()
        findNavController().navigate(directions)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
        /*val searchMenuItem = menu.findItem(R.id.search)
        val searchView: SearchView = searchMenuItem.actionView as SearchView
        search(searchView)*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.tvFav -> {
                callToCurFavFragment()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val searchMenu: MenuItem = menu.findItem(R.id.search)
        searchMenu.isVisible = false
    }

   /* private fun search(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                tvCurAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tvCurAdapter.filter.filter(newText)
                return false
            }
        })
    }*/

    private fun getSearch(){
        tvSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tvCurAdapter.filter.filter(newText)
                return false
            }
        })
    }
}