package com.demo.chris.newscorpdemo.api

import androidx.lifecycle.MutableLiveData
import com.demo.chris.newscorpdemo.data.photos.AlbumPhoto
import com.nochino.support.networking.api.ApiResponse
import retrofit2.http.GET

/**
 * Any calls of type [MutableLiveData] [ApiResponse] require a Retrofit instance
 * to built with a [com.nochino.support.networking.util.MutableLiveDataCallAdapterFactory]
 */
interface ApiPhotos {
    /**
     * @GET declares an HTTP GET request to retrieve the photos
     * from the API.
     */
    @GET("photos/")
    fun getPhotos(): MutableLiveData<ApiResponse<List<AlbumPhoto>>>
}