package com.example.themoviedatabaseapp.utils

interface LoginView {
    fun showErrorMessageForInvalidEmail()
    fun showErrorMessageForInvalidPasswordLength()
    fun showErrorMessageForInvalidPassword()
    fun showErrorMessageForMaxLoginAttempt()
}