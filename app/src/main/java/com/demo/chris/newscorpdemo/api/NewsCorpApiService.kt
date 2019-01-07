package com.demo.chris.newscorpdemo.api

import com.demo.chris.newscorpdemo.data.photos.AlbumPhoto
import retrofit2.Call
import retrofit2.http.GET

interface NewsCorpApiService {
    /**
     * @GET declares an HTTP GET request
     */
    @GET("photos/")
    fun getPhotos(): Call<List<AlbumPhoto>>
}