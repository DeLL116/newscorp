package com.demo.chris.newscorpdemo.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.demo.chris.newscorpdemo.data.photos.PhotoAlbum
import com.demo.chris.newscorpdemo.data.photos.PhotoAlbumRepo
import com.nochino.support.networking.vo.LoadingResource

/**
 * ViewModel containing a [LiveData] object data for a [PhotoAlbum]
 */
class PhotoAlbumViewModel : ViewModel() {

    // TODO :: Inject with Dagger
    private val photoAlbumRepo = PhotoAlbumRepo()

    fun fetchData():LiveData<LoadingResource<PhotoAlbum>> {
        return photoAlbumRepo.getPhotoAlbum()
    }
}