package com.demo.chris.newscorpdemo

import android.app.Application
import com.nochino.retronetworking.RetroNetWorker
import timber.log.Timber

class NewsCorpDemoApplication: Application() {

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