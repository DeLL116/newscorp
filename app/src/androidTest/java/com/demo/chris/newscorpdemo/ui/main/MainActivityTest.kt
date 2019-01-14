package com.demo.chris.newscorpdemo.ui.main

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.demo.chris.newscorpdemo.R
import com.nochino.support.androidui.views.recyclerview.adapters.BaseRecyclerViewAdapter
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    lateinit var mainActivity: MainActivity

    @Before
    fun setUp() {
        mainActivity = mActivityTestRule.activity
    }

    @After
    fun tearDown() {
        // Currently no op
    }

    @Test
    fun rvItemClicksTest() {
        // TODO :: Use Idling Resource!
        Thread.sleep(5500)

        for (i in 0..5) {
            onView(withId(R.id.main_fragment_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, click()))

            pressBack()
        }
    }

    @Test
    fun rvScrollToLastItemTest() {

        // TODO :: Use Idling Resource!
        Thread.sleep(5500)

        //TODO :: Use onData!
        val adapter = mainActivity.findViewById<RecyclerView>(R.id.main_fragment_rv)?.adapter
                as BaseRecyclerViewAdapter<*,*,*>

        onView(withId(R.id.main_fragment_rv))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(adapter.getItemCount()))

        onView(withId(R.id.main_fragment_rv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(adapter.getItemCount() -1, click())
            )

        pressBack()

        mActivityTestRule.activity.runOnUiThread{
            Toast.makeText(mActivityTestRule.activity, "Makes me feel good.", Toast.LENGTH_LONG).show()
        }

        Thread.sleep(2500)
    }
}
