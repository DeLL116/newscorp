package com.demo.chris.newscorpdemo.data.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.chris.newscorpdemo.NewsCorpDemoApplication
import com.demo.chris.newscorpdemo.api.ApiPhotos
import com.nochino.support.networking.repository.NetworkBoundResource
import com.nochino.support.networking.vo.Resource

/**
 * Repository for retrieving and caching data for a [PhotoAlbum]
 *
 * @author Chris Delli Santi
 */
class PhotoAlbumRepo {
    /**
     * Cached [MutableLiveData] object for any previously retrieved [PhotoAlbum]
     */
    private var photoAlbumLiveDataCache = MutableLiveData(PhotoAlbum(emptyMap()))

    // TODO :: Use Dagger
    private val webservice = NewsCorpDemoApplication.instance.retroNetWorker.createWebService(ApiPhotos::class.java)

    // TODO :: Use Dagger
    private val appExecutors = NewsCorpDemoApplication.instance.appExecutors

    /**
     * @return A [LiveData] [Resource] of type [PhotoAlbum].
     */
    fun getPhotoAlbum(): LiveData<Resource<PhotoAlbum>> {
        return object : NetworkBoundResource<PhotoAlbum, List<AlbumPhoto>>(appExecutors) {
            override fun saveCallResult(item: List<AlbumPhoto>) {
                // TODO :: Use DAO Database
//                userDao.insert(item)

                // Populated the photoAlbumLiveDataCache
                photoAlbumLiveDataCache = MutableLiveData(
                    PhotoAlbum(item.map { it.id to it }.toMap() as LinkedHashMap<Int, AlbumPhoto>)
                )
            }

            override fun shouldFetch(data: PhotoAlbum?) = data == null || data.photoAlbumMap.isEmpty()

            override fun loadFromDb() = photoAlbumLiveDataCache

            override fun createCall() = webservice.getPhotos()
        }.asLiveData()
    }
}