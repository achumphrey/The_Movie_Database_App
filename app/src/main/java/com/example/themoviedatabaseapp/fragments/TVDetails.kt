package com.example.themoviedatabaseapp.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.databinding.FragmentTVDetailsBinding
import com.example.themoviedatabaseapp.di.TVShowApp
import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails
import com.example.themoviedatabaseapp.utils.TVDetailsDialogFragment
import com.example.themoviedatabaseapp.utils.TVViewState
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModel
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModelFactory
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class TVDetails : Fragment() {

    private var _binding: FragmentTVDetailsBinding? = null
    private val binding get() = _binding!!
    private var tvId: Int = 0

    @Inject
    lateinit var tvShowViewModelFactory: TVShowViewModelFactory
    private lateinit var tvShowViewModel: TVShowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        TVShowApp.getTVShowComponent().inject(this)

        val args: TVDetailsArgs by navArgs()
        tvId = args.itemID

        tvShowViewModel = ViewModelProvider(
            this,
            tvShowViewModelFactory
        )
            .get(TVShowViewModel::class.java)

        tvShowViewModel.getDetails(tvId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_t_v_details,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* tvShowViewModel.tvDetails().observe(viewLifecycleOwner, { tvObject ->
            binding.tvDetailObject = tvObject
        })

        tvShowViewModel.errorMessage().observe(viewLifecycleOwner, {
            binding.tvDetailsErrorMessage.text = it
        })*/

        /*tvShowViewModel.loadingState.observe(viewLifecycleOwner, {
            when (it) {
                TVShowViewModel.LoadingState.LOADING -> displayProgressbar()
                TVShowViewModel.LoadingState.SUCCESS -> displayTVList()
                TVShowViewModel.LoadingState.ERROR -> displayErrorMessage()
                else -> displayErrorMessage()
            }
        })*/

        tvShowViewModel.viewState.observe(viewLifecycleOwner, {viewState: TVViewState.ViewState ->
            when(viewState){
                is TVViewState.Success -> displayTVList(viewState.tvDetails)
                is TVViewState.ViewLoading -> displayProgressbar()
                is TVViewState.Error -> displayErrorMessage(viewState.message)
            }
        })

        tvShowViewModel.dBAddSuccess?.observe(viewLifecycleOwner, {
            if (it == true) {
                setSnack()
            } else
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
        })

        tvShowViewModel.dbDelSuccess?.observe(viewLifecycleOwner, {
            if (it == true) {
                Toast.makeText(requireContext(), "Data Deleted From DB", Toast.LENGTH_LONG).show()
            } else
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
        })

        val toolbar: Toolbar = requireView().findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
    }

    private fun displayTVList(tvObject: TVShowDetails) {
        binding.tvDetailObject = tvObject
        binding.tvDetailsErrorMessage.visibility = View.GONE
        binding.tvDetailsProgressBar.visibility = View.GONE
    }

    private fun displayErrorMessage(it: String) {
        binding.tvDetailsErrorMessage.text = it
        binding.tvDetailsErrorMessage.visibility = View.VISIBLE
        binding.tvDetailsProgressBar.visibility = View.GONE
    }

    private fun displayProgressbar() {
        binding.tvDetailsProgressBar.visibility = View.VISIBLE
        binding.tvDetailsErrorMessage.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dmenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.dtmenu -> {
                TVDetailsDialogFragment.newInstance(tvId)
                    .show(
                        requireActivity().supportFragmentManager,
                        TVDetailsDialogFragment.TAG)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val menuItem: MenuItem = menu.findItem(R.id.tvFav)
        val searchMenu: MenuItem = menu.findItem(R.id.search)
        searchMenu.isVisible = false
        menuItem.isVisible = false
    }

    @SuppressLint("ShowToast")
    private fun setSnack() {
        val snack = Snackbar.make(requireView(), "Data Added To Database", Snackbar.LENGTH_LONG)
            .setAction("OK") { resetView() }
        snack.setActionTextColor(Color.parseColor("#e3f705"))

        val snackView = snack.view
        snackView.setBackgroundColor(Color.parseColor("#3505f7"))
        val textView =
            snackView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        val actionTextView =
            snackView.findViewById(com.google.android.material.R.id.snackbar_action) as TextView
        textView.setTextColor(Color.parseColor("#FFFFFF"))
        textView.textSize = 25f
        actionTextView.textSize = 25f
        snack.show()
    }

    private fun resetView(){
        requireActivity()
            .supportFragmentManager
            .popBackStackImmediate()
    }
}