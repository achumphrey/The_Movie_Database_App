package com.example.themoviedatabaseapp.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.utils.LoginView
import com.example.themoviedatabaseapp.utils.UserDetailsValidator
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity(), LoginView {

    private lateinit var register: Button
    private lateinit var login: TextView
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userDetailsValidator: UserDetailsValidator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userDetailsValidator = UserDetailsValidator(this)
        register = findViewById(R.id.btnRegister)
        login = findViewById(R.id.btnLogin)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        etEmail.setText(intent.getStringExtra("email"))
        etPassword.setText(intent.getStringExtra("password"))

        // [START initialize_auth]
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        // [END initialize_auth]

        register.setOnClickListener {
            registerUser()
        }

        login.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email: String = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val intent = Intent(
            applicationContext,
            LoginActivity::class.java
        )
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        startActivity(intent)
    }

    private fun registerUser() {
        val email: String = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        if (userDetailsValidator.validateUser(email, password)) {

            // [START create_user_with_email]
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startActivity(
                            Intent(
                                applicationContext,
                                SuccessActivity::class.java
                            )
                        )
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "E-mail or password is wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            // [END create_user_with_email]
        }
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            startActivity(
                Intent(
                    applicationContext,
                    SuccessActivity::class.java
                )
            )
        }
    }
    // [END on_start_check_user]

    override fun showErrorMessageForInvalidEmail() {
        Toast.makeText(
            this,
            "Please fill in the required email address",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showErrorMessageForInvalidPasswordLength() {
        Toast.makeText(
            this,
            "Password must be at least 6 characters",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showErrorMessageForInvalidPassword() {
        Toast.makeText(
            this,
            "Please fill in the required password",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showErrorMessageForMaxLoginAttempt() {
        Toast.makeText(
            this,
            "You have exceeded maximum attempt",
            Toast.LENGTH_SHORT
        ).show()

    }
}