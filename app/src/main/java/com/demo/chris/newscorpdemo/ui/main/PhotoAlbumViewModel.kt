package com.demo.chris.newscorpdemo.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.chris.newscorpdemo.data.photos.PhotoAlbum
import com.demo.chris.newscorpdemo.data.photos.PhotoAlbumRepo

class PhotoAlbumViewModel : ViewModel() {

    private var photoAlbum = MutableLiveData<PhotoAlbum>()

    private val photoAlbumRepo = PhotoAlbumRepo()

    fun fetchData():LiveData<PhotoAlbum> {
        photoAlbum = photoAlbumRepo.getPhotoAlbum()
        return photoAlbum
    }
}