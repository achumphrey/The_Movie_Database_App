package com.example.themoviedatabaseapp.firebase

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.themoviedatabaseapp.R
import com.google.firebase.auth.FirebaseAuth

class LogoutUserDialogFrag : DialogFragment() {

    private lateinit var btnYesLogout: Button
    private lateinit var btnCancelLogout: Button
    private lateinit var tvDialogHeading: TextView
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_logoutuser_dialog,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnYesLogout = view.findViewById(R.id.btnLogoutDialogYes)
        btnCancelLogout = view.findViewById(R.id.btnLogoutDialogCancel)
        tvDialogHeading = view.findViewById(R.id.tvLogoutDialog)
        firebaseAuth = FirebaseAuth.getInstance()
        tvDialogHeading.text = requireActivity().getText(R.string.sure_logout)

        btnCancelLogout.setOnClickListener {
            dismiss()
        }

        btnYesLogout.setOnClickListener {
            logoutUser()
            dismiss()
        }
    }

    private fun logoutUser() {
        firebaseAuth.signOut()
        startActivity(
            Intent(
                requireContext(),
                LoginActivity::class.java)
        )
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    companion object {
        const val TAG = "Dialog Fragment"
    }
}