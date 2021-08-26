package com.example.themoviedatabaseapp.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.di.TVShowApp
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModel
import com.example.themoviedatabaseapp.viewmodel.TVShowViewModelFactory
import javax.inject.Inject

class TVDetailsDialogFragment: DialogFragment() {

    @Inject
    lateinit var tvShowViewModelFactory: TVShowViewModelFactory
    private lateinit var tvShowViewModel: TVShowViewModel

    companion object {
        const val TAG = "DetailsDialog"
        private const val KEY_TV_ID = "TV_ID"
        fun newInstance(tvID: Int): TVDetailsDialogFragment {
            val args = Bundle()
            args.putInt(KEY_TV_ID, tvID)
            val fragment = TVDetailsDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_custom_dialog,
            container,
            false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TVShowApp.getTVShowComponent().inject(this)
        tvShowViewModel = ViewModelProvider(this, tvShowViewModelFactory)
            .get(TVShowViewModel::class.java)

        val tvId = arguments?.get(KEY_TV_ID)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvSubTitle: TextView = view.findViewById(R.id.tvSubTitle)
        tvTitle.text = requireActivity().getString(R.string.dialog_title)
        tvSubTitle.text = requireActivity().getString(R.string.delete_data_from_database)
        val btnPositive: Button = view.findViewById(R.id.btnPositive)
        val btnNegative: Button = view.findViewById(R.id.btnNegative)
        btnPositive.setOnClickListener {
            tvShowViewModel.delShowFromDB(tvId as Int)
            //tvShowViewModel.deleteShowFromFbDb (tvId as Int)
            callReset()
            dismiss()
        }
        btnNegative.setOnClickListener {
            callReset()
            dismiss()
        }
    }

    private fun callReset(){
        requireActivity()
            .supportFragmentManager
            .popBackStackImmediate()
    }

    override fun onStart() {
        super.onStart()
            dialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
    }
}