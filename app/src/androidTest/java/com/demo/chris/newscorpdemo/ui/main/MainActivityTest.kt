package com.demo.chris.newscorpdemo.ui.main

import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.demo.chris.newscorpdemo.R
import com.nochino.support.androidui.views.recyclerview.adapters.BaseRecyclerViewAdapter
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>
    private lateinit var mainActivity: MainActivity

    @Before
    fun setUp() {

        // Launch the Activity
        activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // Gain reference to the activity
        activityScenario.onActivity {
            mainActivity = it
        }

        // Immediately skip the Splash Fragment
        mainActivity
            .findNavController(R.id.nav_host_fragment)
            .navigate(R.id.action_splashFragment_to_mainFragment)
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun rvItemClicksTest() {

        registerIdlingResource()

        for (i in 0..5) {
            onView(withId(R.id.main_fragment_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, click()))

            pressBack()
        }
    }

    // TODO :: Use IdlingCounter in Activity
    private fun registerIdlingResource() {

        // Wait for the fragment animation to finish
        // TODO :: Remove and synchronize idling counter with activity
        Thread.sleep(1000)

        // Find the main fragment.....since Navigation is being used
        // the primaryNavigationFragment is the current fragment of the nav_host_fragment
        val mainFragment = mainActivity
            .supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment)
            ?.childFragmentManager
            ?.primaryNavigationFragment as MainFragment

            IdlingRegistry.getInstance().register(mainFragment.mIdlingRes)
    }

    @Test
    fun rvScrollToLastItemTest() {

        registerIdlingResource()

        val adapter = mainActivity.findViewById<RecyclerView>(R.id.main_fragment_rv)?.adapter
                as BaseRecyclerViewAdapter<*,*,*>

        onView(withId(R.id.main_fragment_rv))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(adapter.getItemCount()))

        onView(withId(R.id.main_fragment_rv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(adapter.getItemCount() -1, click())
            )

        pressBack()

        mainActivity.runOnUiThread{
            Toast.makeText(mainActivity, "Makes me feel good.", Toast.LENGTH_LONG).show()
        }
    }

    @Test
    fun rvTotalCountTest() {

        registerIdlingResource()

        onView(withId(R.id.main_fragment_rv)).check(
            matches(
                RecyclerViewMatchers.withItemCount(5000)
            )
        )
    }
}
