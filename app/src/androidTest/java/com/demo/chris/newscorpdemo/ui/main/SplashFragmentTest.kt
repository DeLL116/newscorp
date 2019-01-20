package com.demo.chris.newscorpdemo.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.demo.chris.newscorpdemo.R
import com.nochino.support.androidui.fragments.BaseFragment
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class SplashFragmentTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>
    private lateinit var mainActivity: MainActivity
    private lateinit var mainFragmentIdlingResource: IdlingResource

    @Before
    fun setUp() {
        // Launch the Activity
        activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // Gain reference to the activity
        activityScenario.onActivity {
            mainActivity = it

            // Get and register the Fragment's IdlingResource.
            // Note...the usage of non-null reference to the idlingResource
            // is purposeful here. This should *never* be null in debuggable builds.
            // See CountingIdlingResourceViewModel's lazy initialization of the object
            // in debuggable builds only!

//            // Increment for View's root creation and display
//            BaseFragment.fragmentViewModelIdlingResource?.incrementTestIdleResourceCounter()
//            // Increment for RecyclerView adapter set with data
//            BaseFragment.fragmentViewModelIdlingResource?.incrementTestIdleResourceCounter()

            // Register the IdlingResource
//            mainFragmentIdlingResource = BaseFragment.fragmentViewModelIdlingResource?.getIdlingResource()!!
            IdlingRegistry.getInstance().register(mainFragmentIdlingResource)
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(mainFragmentIdlingResource)
        activityScenario.close()
    }

    @Test
    fun foo() {
        onView(withId(R.id.splash_root_container)).check(matches(isDisplayed()))
        onView(withId(R.id.splash_logo_header_tv)).check(matches(withText(R.string.app_name)))
        onView(withId(R.id.splash_logo_header_second_tv)).check(matches(withText(R.string.app_desc)))
    }

}