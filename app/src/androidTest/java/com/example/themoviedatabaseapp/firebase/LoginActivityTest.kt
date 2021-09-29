package com.example.themoviedatabaseapp.firebase

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.themoviedatabaseapp.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest{

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes. This is a replacement for {@link androidx.test.rule.ActivityTestRule}.
     */

    @Rule
    @JvmField
    var activityScenarioRule: ActivityScenarioRule<LoginActivity?>? = ActivityScenarioRule(
        LoginActivity::class.java
    )

    @Test
    fun checkIfEmailEditTextIsDisplayed() {
        onView(withId(R.id.et_Email))
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkIfPasswordEditTextIsDisplayed() {
        onView(withId(R.id.et_Password))
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkIfLoginButtonIsDisplayed() {
        onView(withId(R.id.btn_Login))
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkIfRegisterButtonIsDisplayed() {
        onView(withId(R.id.btn_Register))
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkIfEmailAndPasswordAreDisplayedInLogin(){
        onView(withId(R.id.et_Email))
            .perform(ViewActions.typeText("ben"),
                ViewActions.closeSoftKeyboard())
        onView(withId(R.id.et_Password))
            .perform(ViewActions.typeText("tdd"),
                ViewActions.closeSoftKeyboard())
        onView(withId(R.id.btn_Login))
            .check(matches(isDisplayed())).perform(ViewActions.click())
    }
}