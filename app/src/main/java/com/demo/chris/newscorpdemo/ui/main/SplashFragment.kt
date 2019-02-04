package com.demo.chris.newscorpdemo.ui.main

import androidx.navigation.fragment.findNavController
import com.demo.chris.newscorpdemo.R
import com.nochino.support.androidui.fragments.BaseSplashFragment

class SplashFragment : BaseSplashFragment() {

    private var nextFragmentRunnable: Runnable? = Runnable {
        findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
    }

    override fun onResume() {
        super.onResume()
        // After the splash screen duration has elapsed go to the next fragment.
        // If the splash screen duration resource hasn't been defined or can't be derived
        // a default duration of 1.5 seconds is used.
        view?.postDelayed(
            nextFragmentRunnable,
            view?.context?.resources?.getInteger(R.integer.splash_screen_duration_ms)?.toLong() ?: 1500
        )
    }

    override fun onPause() {
        super.onPause()

        // If the fragment is paused before the runnable can execute
        // remove it. The runnable will be re-added in onResume
        view?.removeCallbacks(nextFragmentRunnable)
    }

    override fun onDestroyView() {
        // Clean-up!
        nextFragmentRunnable = null
        super.onDestroyView()
    }
}