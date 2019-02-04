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
        // After 1.5 seconds go to the next fragment
        view?.postDelayed(nextFragmentRunnable, 1500)
    }

    override fun onPause() {
        super.onPause()

        // If the fragment is paused before the runnable can execute
        // remove it.
        view?.removeCallbacks(nextFragmentRunnable)
    }

    override fun onDestroyView() {
        // Clean-up!
        nextFragmentRunnable = null
        super.onDestroyView()
    }
}