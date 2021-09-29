package com.example.themoviedatabaseapp.utils

import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserDetailsValidatorTest{

    private lateinit var userDetailsValidator: UserDetailsValidator
    private val loginView: LoginView = mock(LoginView::class.java)

    @BeforeEach
    fun setup(){
        userDetailsValidator = UserDetailsValidator(loginView)
    }

    @Test
    fun checkIfLoginFailedAttemptIsExceeded() {
        Assert.assertEquals(1, userDetailsValidator.incrementLoginAttempt())
        Assert.assertEquals(2, userDetailsValidator.incrementLoginAttempt())
        Assert.assertEquals(3, userDetailsValidator.incrementLoginAttempt())
        Assert.assertTrue(userDetailsValidator.isLoginFailedAttemptExceeded())
    }

    @Test
    fun checkIfLoginFailedAttemptIsNotExceeded() {
        Assert.assertFalse(userDetailsValidator.isLoginFailedAttemptExceeded())
    }

    @Test
    fun checkIfUsernameAndPasswordAreCorrect(){
        Assert.assertTrue(userDetailsValidator.validateUser("ben@gmail.com", "ben1234"))
    }

    @Test
    fun checkIfEmailIsInCorrect(){
        userDetailsValidator.validateUser("", "ben1234")
        verify(loginView, atLeast(1))
            .showErrorMessageForInvalidEmail()
    }

    @Test
    fun checkIfPasswordIsInCorrect(){
        userDetailsValidator.validateUser("ben@gmail.com", "")
        verify(loginView, atLeast(1))
            .showErrorMessageForInvalidPassword()
    }

    @Test
    fun checkIfPasswordLengthIsInCorrect(){
        userDetailsValidator.validateUser("ben@gmail.com", "ben12")
        verify(loginView, atLeast(1))
            .showErrorMessageForInvalidPasswordLength()
    }

    @Test
    fun checkIfLoginFailedAttemptIsExceededAndViewMethodIsCalled() {
        userDetailsValidator.validateUser("XYZ", "tdd")
        userDetailsValidator.validateUser("XYZ", "tdd")
        userDetailsValidator.validateUser("XYZ", "tdd")
        userDetailsValidator.validateUser("XYZ", "tdd")
        verify(loginView, atLeast(1))
            .showErrorMessageForMaxLoginAttempt()
    }
}