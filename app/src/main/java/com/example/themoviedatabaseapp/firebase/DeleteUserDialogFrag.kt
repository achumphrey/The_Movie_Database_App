package com.example.themoviedatabaseapp.firebase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.themoviedatabaseapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class DeleteUserDialogFrag : DialogFragment() {

    private lateinit var tvDelUser: TextView
    private lateinit var btnDelUserYes: Button
    private lateinit var btnDelUserCancel: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var activity: AppCompatActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_deleteuser_dialog,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvDelUser = view.findViewById(R.id.tvDeleteDialog)
        btnDelUserCancel = view.findViewById(R.id.btnDelDialogCancel)
        btnDelUserYes = view.findViewById(R.id.btnDeleteDialogYes)

        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser

        tvDelUser.text = requireActivity().getText(R.string.delete_user_dialog)

        btnDelUserCancel.setOnClickListener {
            dismiss()
        }

        btnDelUserYes.setOnClickListener {
            user?.delete()
            startActivity(
                Intent(
                    activity,
                    RegisterActivity::class.java
                )
            )
            activity.finish()
            dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppCompatActivity)
            activity = context
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