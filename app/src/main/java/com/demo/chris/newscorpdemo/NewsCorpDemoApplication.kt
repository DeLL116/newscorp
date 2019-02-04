package com.demo.chris.newscorpdemo

import com.demo.chris.newscorpdemo.data.photo.PhotoAlbumRepo
import com.google.gson.GsonBuilder
import com.nochino.support.androidui.AndroidUIApplication
import com.nochino.support.networking.util.MutableLiveDataCallAdapterFactory
import com.nochino.support.networking.RetroNetWorker
import com.nochino.support.networking.execution.AppExecutors
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class NewsCorpDemoApplication: AndroidUIApplication() {

    /**
     * Creates the API object with the base API URL using lazy initialization
     * TODO :: Use Dagger
     */
    val retroNetWorker: RetroNetWorker by lazy {
        RetroNetWorker(
            getString(R.string.api_base_url),
            MutableLiveDataCallAdapterFactory(),
            GsonConverterFactory.create(GsonBuilder().create())
        )
    }

    /**
     * Executors used for DiskIO, NetworkIO, and the app's main thread
     * TODO :: Use Dagger
     */
    val appExecutors: AppExecutors by lazy {
        AppExecutors()
    }

    /**
     * Single source of truth repository for data associated with the Photos API
     * TODO :: Use Dagger
     */
    val photoAlbumRepo: PhotoAlbumRepo by lazy {
        PhotoAlbumRepo()
    }

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