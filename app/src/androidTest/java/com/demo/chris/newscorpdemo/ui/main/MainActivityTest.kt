package com.demo.chris.newscorpdemo.ui.main

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.demo.chris.newscorpdemo.R
import com.nochino.support.androidui.testing.OrientationChangeAction
import com.nochino.support.androidui.fragments.BaseFragment
import com.nochino.support.androidui.testing.CountingIdlingResourceViewModel
import com.nochino.support.androidui.testing.RecyclerViewMatchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    // The known size of the data set returned for the RecyclerView in this fragment
    private val recyclerViewAdapterSize: Int = 5000

    private lateinit var activityScenario: ActivityScenario<MainActivity>
    private lateinit var activityIdlingResourceViewModel: CountingIdlingResourceViewModel
    private var currentFragmentIdlingResourceViewModel : CountingIdlingResourceViewModel? = null

    @Before
    fun setUp() {

        // Launch the Activity
        activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // Gain reference to the activity
        activityScenario.onActivity {
            // Get and register the Activity's IdlingResource.
            // Note...the usage of non-null reference to the idlingResource
            // is purposeful here. This should *never* be null in debuggable builds.
            // See CountingIdlingResourceViewModel's lazy initialization of the object
            // for debuggable builds only!
            activityIdlingResourceViewModel = it.activityViewModelIdlingResource!!
            IdlingRegistry.getInstance().register(activityIdlingResourceViewModel.idlingResource)
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(activityIdlingResourceViewModel.idlingResource)
        IdlingRegistry.getInstance().unregister(currentFragmentIdlingResourceViewModel?.idlingResource)
        activityScenario.onActivity {
            Timber.d("Tearing down activity {${it.hashCode()}")
        }
        activityScenario.close()
    }

    @Test
    fun splashFragmentTest() {
        // Ensure the SplashFragment's ViewGroup is displayed
        onSplashFragmentDisplayed()
    }

    @Test
    fun mainFragmentTest() {
        onMainFragmentDisplayed()
    }

    @Test
    fun mainActivityWalkThroughTest() {

        // Ensure the Splash Fragment is displayed
        onSplashFragmentDisplayed()

        // Ensure the Main Fragment is displayed
        onMainFragmentDisplayed()

        // There is a new Fragment (MainFragment) now attached to the Activity
        // Ensure the current fragment's IdlingResource is added to the IdleRegistry
        registerNewFragmentIdlingResource()

        onRecyclerViewPopulated()

        // Check clicking the RecyclerView items at the range provided
        rvItemClicksTest(0, 5)

        // Verify the size of the RecyclerView's adapter (this is a strict test of a known data-set size)
        rvTotalCountTest(recyclerViewAdapterSize)

        // Verify scrolling to the last item in the RecyclerView
        rvScrollToItemTest(recyclerViewAdapterSize - 1, withClick = true, returnFromClick = true)

        activityScenario.onActivity {
            it.runOnUiThread {
                Toast.makeText(it, "Makes me feel good.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onSplashFragmentDisplayed() {
        registerNewFragmentIdlingResource()
        onView(withId(R.id.splash_root_container)).check(matches(isDisplayed()))

        val orientations: Array<ViewAction> = arrayOf(
            OrientationChangeAction.orientationLandscape(),
            OrientationChangeAction.orientationPortrait()
        )
        for (i in 0..3) {
            // Rotate 4 times using a different orientation from the array each time
            rotate(orientations[i % orientations.size])
        }

        // Ensure the logo is entirely visible...we don't want to cut off our logo!
        onView(withId(R.id.splash_logo_iv)).check(matches(isCompletelyDisplayed()))

        // Ensure the splash header depicts the string defined at app_name and is completely displayed
        onView(withId(R.id.splash_header_tv)).check(matches(withText(R.string.app_name)))
        onView(withId(R.id.splash_header_tv)).check(matches(isCompletelyDisplayed()))

        // Ensure the splash secondary header depicts the string defined at app_desc and is completely displayed
        onView(withId(R.id.splash_header_secondary_tv)).check(matches(withText(R.string.app_desc)))
        onView(withId(R.id.splash_header_secondary_tv)).check(matches(isCompletelyDisplayed()))

        // Splash Fragment's layout uses layout-weights for proper re-sizing and placements
        // ....ensure the child views are properly nested in the layout's defined XML
        onView(withId(R.id.splash_logo_iv)).check(matches(isDescendantOfA(withId(R.id.splash_root_container))))
        onView(withId(R.id.splash_header_tv)).check(matches(isDescendantOfA(withId(R.id.splash_root_container))))
        onView(withId(R.id.splash_header_secondary_tv)).check(matches(isDescendantOfA(withId(R.id.splash_root_container))))
    }

    private fun onMainFragmentDisplayed() {
        // To run any tests on MainFragment, Espresso must be halted
        // until the SplashFragment has exited and MainFragment's root View
        // has been inflated / added to the View hierarchy. Increment the
        // Activity's CountingIdlingResourceCounter to instruct Espresso to
        // wait.

        // Instruct Espresso will wait until the View assertion for MainFragment's
        // root ViewGroup can be performed
        activityIdlingResourceViewModel.incrementTestIdleResourceCounter()

        // ---> activityIdlingResourceViewModel is decremented in
        //      MainFragment.onViewCreated() to inform Espresso it's OK
        //      to proceed with view assertion made on main_fragment_root
        onView(withId(R.id.main_fragment_root)).check(matches(isDisplayed()))
    }

    private fun onRecyclerViewPopulated() {
        // To run any tests on MainFragment's RecyclerView, Espresso must be halted
        // until the RecyclerView has been displayed by MainFragment and populated with
        // data.
        //
        // MainFragment

        // Wait until the View assertion for MainFragment's RecyclerView
        // can be performed
        // ---> currentFragmentIdlingResourceViewModel is decremented in
        //      MainFragment.updateAdapter() to allow Espresso to
        //      proceed with view assertion made in onRecyclerViewPopulated()
        onView(withId(R.id.main_fragment_rv)).check(matches(isDisplayed()))
    }


    private fun rvItemClicksTest(fromItem: Int = 0, toItem: Int = 5) {
        for (i in fromItem..toItem) {
            rvScrollToItemTest(i, withClick = true, returnFromClick = true)
        }
    }

    private fun rvScrollToItemTest(pos: Int, withClick: Boolean = false, returnFromClick: Boolean = false) {

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

    private fun registerNewFragmentIdlingResource() {

        activityScenario.onActivity {
            // Get the instance of the new fragment's IdlingResource
            val baseFragment = it
                .supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment)
                ?.childFragmentManager
                ?.primaryNavigationFragment as BaseFragment

            val newFragIdlingResViewModel = baseFragment.fragmentViewModelIdlingResource!!

            if (currentFragmentIdlingResourceViewModel != null
                && newFragIdlingResViewModel !== currentFragmentIdlingResourceViewModel) {
                IdlingRegistry.getInstance().unregister(currentFragmentIdlingResourceViewModel!!.idlingResource)
            }

            if (newFragIdlingResViewModel !== currentFragmentIdlingResourceViewModel) {
                currentFragmentIdlingResourceViewModel = newFragIdlingResViewModel
            } else {
                // TOdo :: Be mean + throw exception...or be nice and silently finish?
            }

            IdlingRegistry.getInstance().register(currentFragmentIdlingResourceViewModel!!.idlingResource)
        }
    }

    private fun rotate(orientationAction: ViewAction) {
        onView(isRoot()).perform(orientationAction)
        getInstrumentation().waitForIdleSync()
        Thread.sleep(750)
    }
}