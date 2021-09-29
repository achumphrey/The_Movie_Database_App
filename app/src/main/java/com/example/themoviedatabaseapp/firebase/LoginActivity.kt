package com.example.themoviedatabaseapp.firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.themoviedatabaseapp.R
import com.example.themoviedatabaseapp.utils.LoginView
import com.example.themoviedatabaseapp.utils.UserDetailsValidator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity(), LoginView {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var newPassButton: TextView
    private lateinit var signInButton: SignInButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var launchStartActivity: ActivityResultLauncher<Intent>
    private lateinit var userDetailsValidator: UserDetailsValidator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userDetailsValidator = UserDetailsValidator(this)
        loginButton = findViewById(R.id.btn_Login)
        etEmail = findViewById(R.id.et_Email)
        etPassword = findViewById(R.id.et_Password)
        registerButton = findViewById(R.id.btn_Register)
        newPassButton = findViewById(R.id.btn_ForgotPassword)
        signInButton = findViewById(R.id.sign_in_button)
        firebaseAuth = FirebaseAuth.getInstance()

        etEmail.setText(intent.getStringExtra("email"))
        etPassword.setText(intent.getStringExtra("password"))

        // [START config_signIn]
        // Configure Google Sign In
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // [END config_signIn]

        signInButton.setOnClickListener {
            callSignIn()
        }

        registerButton.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val intent = Intent(
                applicationContext,
                RegisterActivity::class.java
            )
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            startActivity(intent)
        }

        newPassButton.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    NewPasswordActivity::class.java
                )
            )
        }

        loginButton.setOnClickListener {
            loginUser()
        }

        launchStartActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    authWithGoogle(account)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e)
                }
            }
        }
    }

    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        if (userDetailsValidator.validateUser(email, password)) {

            // [START sign_in_with_email]
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        startActivity(
                            Intent(
                                applicationContext,
                                SuccessActivity::class.java
                            )
                        )
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            // [END sign_in_with_email]
        }
    }

    // [START on_start_check_user]
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            startActivity(Intent(baseContext, SuccessActivity::class.java))
        }
    }
    // [END on_start_check_user]

    // [START signIn]
    private fun callSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        launchStartActivity.launch(signInIntent)
    }
    // [END signIn]

    // [START auth_with_google]
    private fun authWithGoogle(account: GoogleSignInAccount) {
        val credential: AuthCredential =
            GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, start SuccessActivity with the signed-in user's information
                    startActivity(Intent(applicationContext, SuccessActivity::class.java))
                    Log.d(TAG, "signInWithCredential:success")
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext, "Auth Error", Toast.LENGTH_SHORT).show()
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }
    // [END auth_with_google]

    companion object {
        private const val TAG = "GoogleActivity"
    }

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