package com.demo.chris.newscorpdemo.ui.main

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.demo.chris.newscorpdemo.R
import com.nochino.support.androidui.fragments.BaseFragment
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    // The known size of the data set returned for the RecyclerView in this fragment
    private val recyclerViewAdapterSize: Int = 5000

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
            // Increment for RecyclerView adapter set with data
            mainFragmentIdlingResource = BaseFragment.fragmentViewModelIdlingResource?.getIdlingResource()!!
            IdlingRegistry.getInstance().register(mainFragmentIdlingResource)
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(mainFragmentIdlingResource)
        activityScenario.close()
    }

    @Test
    fun mainFragmentRecyclerViewAllTests() {

        // The Fragment's IdlingResource counter is incremented before performing view assertion
        // on the fragment's RecyclerView. This IdlingResource counter is decremented when
        // either the data needed to populate the RecyclerView has been provided to the Fragment
        // and the RecyclerView adapter is populated.
        BaseFragment.fragmentViewModelIdlingResource?.incrementTestIdleResourceCounter()

        // Check to ensure the RecyclerView has been displayed
        onRecyclerViewPopulated()

        // Check clicking the RecyclerView items at the range provided
        rvItemClicksTest(0, 5)

        // Verify the size of the RecyclerView's adapter (this is a strict test of a known data-set size)
        rvTotalCountTest(recyclerViewAdapterSize)

        // Verify scrolling to the last item in the RecyclerView
        rvScrollToItemTest(recyclerViewAdapterSize - 1, withClick = true, returnFromClick = true)

        mainActivity.runOnUiThread{
            Toast.makeText(mainActivity, "Makes me feel good.", Toast.LENGTH_LONG).show()
        }
    }

    private fun onRecyclerViewPopulated() {
        onView(withId(R.id.main_fragment_rv)).check(matches(isDisplayed()))
    }


    private fun rvItemClicksTest(fromItem: Int = 0, toItem: Int = 5) {
        for (i in fromItem..toItem) {
            rvScrollToItemTest(i, withClick = true, returnFromClick = true)
        }
    }

    private fun rvScrollToItemTest(pos: Int, withClick: Boolean = false, returnFromClick: Boolean = false) {

//        val adapter = mainActivity.findViewById<RecyclerView>(R.id.main_fragment_rv)?.adapter
//                as BaseRecyclerViewAdapter<*,*,*>

        // Scroll to the RecyclerView to the item at the param position provided.
        // If provided click boolean param is true the item will be clicked as well (see let)
        onView(withId(R.id.main_fragment_rv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    pos, withClick.let {
                        return@let click()
                    }
                )
            )

        // Invoke a back-press as per the provided boolean param
        when (returnFromClick) {
            true -> pressBack()
        }
    }

    private fun rvTotalCountTest(adapterSize: Int) {

        onView(withId(R.id.main_fragment_rv)).check(
            matches(
                RecyclerViewMatchers.withItemCount(adapterSize)
            )
        )
    }
}