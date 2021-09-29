package com.example.themoviedatabaseapp.firebase

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.themoviedatabaseapp.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterActivityTest{

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes. This is a replacement for {@link androidx.test.rule.ActivityTestRule}.
     */

    @Rule
    @JvmField
    var activityScenarioRule: ActivityScenarioRule<RegisterActivity?>? = ActivityScenarioRule(
        RegisterActivity::class.java
    )

    @Test
    fun checkIfEmailEditTextIsDisplayed() {
        onView(withId(R.id.etEmail)).check(matches(isDisplayed()))
    }

    @Test
    fun checkIfPasswordEditTextIsDisplayed() {
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()))
    }

    @Test
    fun checkIfRegisterButtonIsDisplayed() {
        onView(withId(R.id.btnRegister)).check(matches(isDisplayed()))
    }

    @Test
    fun checkIfEmailAndPasswordAreDisplayedInRegister(){
        onView(withId(R.id.etEmail)).perform(typeText("ben"), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText("tdd"), closeSoftKeyboard())
        onView(withId(R.id.btnRegister)).check(matches(isDisplayed())).perform(click())
    }
}