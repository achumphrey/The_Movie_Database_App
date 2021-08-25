package com.example.themoviedatabaseapp.firebase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.themoviedatabaseapp.R
import com.google.firebase.auth.FirebaseAuth

class NewPasswordActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var btnNewPass: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)

        firebaseAuth = FirebaseAuth.getInstance()
        etEmail = findViewById(R.id.etYourEmail)
        btnNewPass = findViewById(R.id.btnNewPassword)

        btnNewPass.setOnClickListener {
            val myEmail = etEmail.text.toString()
            if (myEmail.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please Enter your Email",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@setOnClickListener
            }

            firebaseAuth.sendPasswordResetEmail(myEmail)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Password reset link was sent your email address",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Mail sending error",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
        }
    }
}