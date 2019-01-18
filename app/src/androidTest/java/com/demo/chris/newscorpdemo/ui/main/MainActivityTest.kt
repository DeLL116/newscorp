package com.demo.chris.newscorpdemo.ui.main

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResourceTimeoutException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.demo.chris.newscorpdemo.R
import com.nochino.support.androidui.activities.BaseActivity
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
    private lateinit var mainActivityIdlingResource: IdlingResource
    private lateinit var mainFragmentIdlingResource: IdlingResourceTimeoutException

    @Before
    fun setUp() {

        // Launch the Activity
        activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // Gain reference to the activity
        activityScenario.onActivity {
            mainActivity = it

            // Get and register the Activity IdlingResource.
            // Note...the usage of non-null reference to the idlingResource
            // is purposeful here. This should *never* be null in debuggable builds.
            // See CountingIdlingResourceViewModel's lazy initialization of the object
            // in debuggable builds only!
            mainActivityIdlingResource = BaseActivity.activityViewModelIdlingResource?.getIdlingResource()!!
            IdlingRegistry.getInstance().register(mainActivityIdlingResource)
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(mainActivityIdlingResource)
        activityScenario.close()
    }

    @Test
    fun rvItemClicksTest() {

        waitForMainFragment()

        for (i in 0..5) {
            onView(withId(R.id.main_fragment_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, click()))

            pressBack()
        }
    }


    @Test
    fun rvScrollToLastItemTest() {

        waitForMainFragment()

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

        waitForMainFragment()

        onView(withId(R.id.main_fragment_rv)).check(
            matches(
                RecyclerViewMatchers.withItemCount(5000)
            )
        )
    }

    private fun waitForMainFragment() {

        // Wait for the fragment animation to finish
//        EspressoWaiter(object : Wait.Condition {
//            override fun check(): Boolean {
//                return mainActivity
//                    .supportFragmentManager
//                    .findFragmentById(R.id.nav_host_fragment)
//                    ?.childFragmentManager
//                    ?.primaryNavigationFragment is MainFragment
//            }
//        }).waitForIt()

        // Manually invoke Espresso OnIdle to wait for
        // the registered IdlingResource objects. OnIdle() needs to be
        // invoked manually because directly below we are not making
        // an espresso call, but trying to find a fragment via regular
        // (non-espresso) code.
        onIdle()

        // Find the main fragment.....since Navigation is being used
        // the primaryNavigationFragment is the current fragment of the nav_host_fragment
        val mainFragment = mainActivity
            .supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment)
            ?.childFragmentManager
            ?.primaryNavigationFragment as MainFragment


        IdlingRegistry.getInstance().register(mainFragment.photoAlbumIdlingResource)
    }
}
