package com.demo.chris.newscorpdemo.api

import androidx.lifecycle.LiveData
import com.demo.chris.newscorpdemo.data.photos.AlbumPhoto
import com.nochino.support.networking.api.ApiResponse
import retrofit2.http.GET

/**
 * Any calls of type [LiveData] [ApiResponse] require a Retrofit instance
 * to built with a [com.nochino.support.networking.util.LiveDataCallAdapterFactory]
 */
interface ApiPhotos {
    /**
     * @GET declares an HTTP GET request to retrieve the photos
     * from the API.
     */
    @GET("photos/")
    fun getPhotos(): LiveData<ApiResponse<List<AlbumPhoto>>>
}