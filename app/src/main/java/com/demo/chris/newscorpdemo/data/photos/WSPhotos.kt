package com.demo.chris.newscorpdemo.data.photos

import com.demo.chris.newscorpdemo.data.photos.AlbumPhoto
import io.reactivex.Observable
import retrofit2.http.GET

interface WSPhotos {
    @GET("photos/")
    fun getPhotos(): Observable<List<AlbumPhoto>>
}