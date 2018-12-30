package com.demo.chris.newscorpdemo.data.photos

import retrofit2.Call
import retrofit2.http.GET

interface WSPhotos {
    /**
     * @GET declares an HTTP GET request
     */
    @GET("photos/")
    fun getPhotos(): Call<List<AlbumPhoto>>
}