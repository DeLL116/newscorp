package com.demo.chris.newscorpdemo.ui.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.demo.chris.newscorpdemo.R
import com.nochino.support.androidui.activities.BaseActivity
import com.nochino.support.androidui.fragments.BaseSplashFragment

class SplashFragment : BaseSplashFragment() {

    // TODO :: Move navigation responsibility to BaseSplashFragment
    private var nextFragmentRunnable: Runnable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO :: Move to "Staging Debug" Flavor class variant (don't keep in production code)!
        BaseActivity.activityViewModelIdlingResource?.incrementTestIdleResourceCounter()

        nextFragmentRunnable = Runnable {
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        view?.postDelayed(nextFragmentRunnable, 1500)
    }

    override fun onPause() {
        super.onPause()
        view?.removeCallbacks(nextFragmentRunnable)
    }

    override fun onDestroyView() {
        nextFragmentRunnable = null
        super.onDestroyView()
    }
}