package com.demo.chris.newscorpdemo.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.chris.newscorpdemo.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // TODO : Create Abstract class for ToolBar Activity
        setSupportActionBar(findViewById(R.id.base_activity_toolbar))
    }
}
