package com.demo.chris.newscorpdemo.ui.main

import com.demo.chris.newscorpdemo.R
import com.nochino.support.androidui.activities.BaseMobileToolbarActivity

class MainActivity : BaseMobileToolbarActivity() {
    override fun getLayoutId(): Int {
        return R.layout.main_activity
    }
}