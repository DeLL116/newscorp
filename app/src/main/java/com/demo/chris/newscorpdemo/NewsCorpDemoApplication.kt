package com.demo.chris.newscorpdemo

import com.nochino.support.androidui.AndroidUIApplication
import com.nochino.support.networking.RetroNetWorker
import timber.log.Timber

class NewsCorpDemoApplication: AndroidUIApplication() {

    /**
     * Creates the API object with the base API URL using lazy initialization
     */
    val retroNetWorker: RetroNetWorker by lazy { RetroNetWorker(getString(R.string.api_base_url)) }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // TODO :: WIP :: Create Crash Reporting Tree
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        lateinit var instance: NewsCorpDemoApplication
    }
}