package com.example.themoviedatabaseapp.utils

class UserDetailsValidator(private val loginView: LoginView) {

    private var loginAttempt: Int = 0

    fun incrementLoginAttempt(): Int {
        loginAttempt += 1
        return loginAttempt
    }

    fun isLoginFailedAttemptExceeded(): Boolean {
        return loginAttempt >= MAX_FAILED_LOGIN_ATTEMPT
    }

    fun validateUser(email: String, password: String): Boolean {

        if (isLoginFailedAttemptExceeded()) {
            loginView.showErrorMessageForMaxLoginAttempt()
            return false
        } else {

            if (email.isEmpty()) {
                incrementLoginAttempt()
                loginView.showErrorMessageForInvalidEmail()
                return false
            }
            if (password.isEmpty()) {
                incrementLoginAttempt()
                loginView.showErrorMessageForInvalidPassword()
                return false
            }
            if (password.length < 6) {
                incrementLoginAttempt()
                loginView.showErrorMessageForInvalidPasswordLength()
                return false
            }
        }
        return true
    }

    companion object {
        private const val MAX_FAILED_LOGIN_ATTEMPT = 3
    }
}