package com.demo.chris.newscorpdemo.data.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.chris.newscorpdemo.NewsCorpDemoApplication
import com.demo.chris.newscorpdemo.api.ApiPhotos
import com.nochino.support.networking.api.ApiEmptyResponse
import com.nochino.support.networking.api.ApiErrorResponse
import com.nochino.support.networking.api.ApiResponse
import com.nochino.support.networking.api.ApiSuccessResponse
import com.nochino.support.networking.repository.NetworkBoundResource
import com.nochino.support.networking.vo.LoadingResource

/**
 * Repository for retrieving and caching data for a [PhotoAlbum]
 *
 * @author Chris Delli Santi
 */
class PhotoAlbumRepo {
    /**
     * [MutableLiveData] object of a [PhotoAlbum] belonging to this repository. This object
     * is constructed by calls to retrieve a [List] of [AlbumPhoto] objects.
     */
    private val photoAlbumLiveData = MutableLiveData(PhotoAlbum(emptyMap()))

    /**
     * List holding any failed attempts for an API request to the list of [AlbumPhoto] objects
     * (which is the backing-data to the [photoAlbumLiveData])
     */
    private var failedRequestsList: MutableSet<MutableLiveData<ApiResponse<List<AlbumPhoto>>>> = mutableSetOf()

    // TODO :: Use Dagger
    private val webservice = NewsCorpDemoApplication.instance.retroNetWorker.createWebService(ApiPhotos::class.java)

    // TODO :: Use Dagger
    private val appExecutors = NewsCorpDemoApplication.instance.appExecutors

    /**
     * @return A [LiveData] [LoadingResource] of type [PhotoAlbum].
     * @param ignoreCache True to explicitly ignore any cached data
     */
    fun getPhotoAlbum(ignoreCache: Boolean = false): LiveData<LoadingResource<PhotoAlbum>> {

        return object : NetworkBoundResource<PhotoAlbum, List<AlbumPhoto>>(appExecutors) {

            override fun preProcessRawResponse(liveDataRequestResponse: MutableLiveData<ApiResponse<List<AlbumPhoto>>>) {

                when (liveDataRequestResponse.value) {
                    is ApiEmptyResponse, is ApiErrorResponse -> {
                        // The request returned as a failure...
                        // keep it's reference for when future requests return successful
                        failedRequestsList.add(liveDataRequestResponse)
                    }
                }
            }

            override fun processSuccessResponse(apiSuccessResponse: ApiSuccessResponse<List<AlbumPhoto>>): List<AlbumPhoto> {

                failedRequestsList.iterator().forEachRemaining {
                    // Post the successful response to any observers of previously failed attempts
                    it.postValue(apiSuccessResponse)
                }.also {
                    // After posting to all observers...clear the list.
                    failedRequestsList.clear()
                }

                return super.processSuccessResponse(apiSuccessResponse)
            }

            override fun saveCallResult(item: List<AlbumPhoto>) {
                // TODO :: Use DAO Database
//                userDao.insert(item)

                // Update the vale of the PhotoAlbumLiveData object with the new list
                // of photos. Note...processing of large maps here is  OK because this
                // happens on a background thread
                photoAlbumLiveData.value?.photoAlbumMap =
                    item.map { it.id to it }.toMap() as LinkedHashMap<Int, AlbumPhoto>
            }

            override fun shouldFetch(data: PhotoAlbum?) = ignoreCache || data == null || data.photoAlbumMap.isEmpty()

            override fun loadFromStorage() = photoAlbumLiveData

            override fun createCall() = webservice.getPhotos()

        }.asLiveData()
    }
}