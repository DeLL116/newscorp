package com.demo.chris.newscorpdemo.ui.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.demo.chris.newscorpdemo.R
import com.nochino.support.androidui.fragments.BaseSplashFragment

class SplashFragment : BaseSplashFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        }, 1500)
    }
}