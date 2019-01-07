package com.demo.chris.newscorpdemo.data.photos

import androidx.lifecycle.MutableLiveData
import com.demo.chris.newscorpdemo.NewsCorpDemoApplication
import com.demo.chris.newscorpdemo.api.NewsCorpApiService
import com.nochino.support.networking.DataCache
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * Repository for retrieving and caching data for a [PhotoAlbum]
 *
 * @author Chris Delli Santi
 */
class PhotoAlbumRepo {
    /**
     * Cached [MutableLiveData] for any previously retrieved [PhotoAlbum]
     */
    private var photoAlbumCache: DataCache<MutableLiveData<PhotoAlbum>> =
        DataCache(MutableLiveData())

    /**
     * @param ignoreCache True to ignore any cache data and explicitly request the
     * [PhotoAlbum] data from the network resource. Default is false (always returns
     * cached data if it exists)
     *
     * @return A [PhotoAlbum] object containing a data set (list) of photos.
     */
    fun getPhotoAlbum(ignoreCache: Boolean = false): MutableLiveData<PhotoAlbum> {

        if (!ignoreCache && photoAlbumCache.cache.value != null) {
            Timber.d("Returned PhotoAlbum data from cache")
            return photoAlbumCache.cache
        }

        Timber.d("PhotoAlbum cache is empty or explicitly ignored...fetching data from network")

        // Instantiate the LiveData object that is returned to the observer
        val photoAlbumLiveData = MutableLiveData<PhotoAlbum>()

        // TODO :: Create helper function to create a WebService request
        val webservice =
            NewsCorpDemoApplication.instance.retroNetWorker.createWebService(NewsCorpApiService::class.java)

        // Call to the webservice to retrieve the PhotoAlbum Data
        webservice.getPhotos().enqueue(object : Callback<List<AlbumPhoto>> {
            override fun onFailure(call: Call<List<AlbumPhoto>>, t: Throwable) {
                // TODO :: Handle failures
            }

            override fun onResponse(call: Call<List<AlbumPhoto>>, response: Response<List<AlbumPhoto>>) {
                // Create a new PhotoAlbum from the network response
                val photoAlbum = PhotoAlbum(response.body()!!)

                // Cache the retrieved data
                photoAlbumCache.cache.value = photoAlbum

                // Post the returned value to any observers of PhotoAlbum
                photoAlbumLiveData.postValue(photoAlbumCache.cache.value)
            }
        })
        return photoAlbumLiveData
    }
}