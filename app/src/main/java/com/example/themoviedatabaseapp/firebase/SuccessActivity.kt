package com.example.themoviedatabaseapp.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.themoviedatabaseapp.MainActivity
import com.example.themoviedatabaseapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SuccessActivity : AppCompatActivity() {

    private lateinit var txtEmail: TextView
    private lateinit var btnDeleteUser: Button
    private lateinit var btnLogout: Button
    private lateinit var btnContinue: Button
    private lateinit var updateEmail: Button
    private lateinit var updatePassword: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        btnContinue = findViewById(R.id.btnContinue)
        txtEmail = findViewById(R.id.tvEmail)
        btnDeleteUser = findViewById(R.id.btnDelUser)
        btnLogout = findViewById(R.id.btnLogout)
        updateEmail = findViewById(R.id.btnUpdateEmail)
        updatePassword = findViewById(R.id.btnUpdatePassword)
        firebaseAuth = FirebaseAuth.getInstance()

        btnContinue.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        authStateListener = FirebaseAuth.AuthStateListener {
            val user: FirebaseUser? = it.currentUser
            if (user == null) {
                startActivity(
                    Intent(
                        applicationContext,
                        LoginActivity::class.java
                    )
                )
                finish()
            }
        }

        val user: FirebaseUser? = firebaseAuth.currentUser
        txtEmail.text = String.format("Hi " + user?.email)

        btnDeleteUser.setOnClickListener {
            user?.delete()?.addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "User Deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        Intent(
                            applicationContext,
                            RegisterActivity::class.java
                        )
                    )
                    finish()
                }else{
                    Toast.makeText(
                        applicationContext,
                        "Nothing to delete",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(
                Intent(
                    applicationContext,
                    LoginActivity::class.java
                )
            )
            finish()
        }

        updateEmail.setOnClickListener {
            EmailDialogFrag().show(supportFragmentManager, EmailDialogFrag.TAG)
        }

        updatePassword.setOnClickListener {
            PasswordDialogFrag().show(supportFragmentManager, PasswordDialogFrag.TAG)
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener { authStateListener }
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener { authStateListener }
    }
}