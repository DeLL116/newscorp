package com.demo.chris.newscorpdemo.ui.main

import androidx.lifecycle.LiveData
import com.demo.chris.newscorpdemo.NewsCorpDemoApplication
import com.demo.chris.newscorpdemo.data.photos.PhotoAlbum
import com.nochino.support.networking.vo.LoadingResourceViewModel
import com.nochino.support.networking.vo.LoadingResource

/**
 * ViewModel containing a [LiveData] object data for a [PhotoAlbum]
 */
class PhotoAlbumViewModel : LoadingResourceViewModel<PhotoAlbum>() {

    // TODO :: Inject with Dagger
    private val photoAlbumRepo = NewsCorpDemoApplication.instance.photoAlbumRepo

    override fun fetchLiveData(): LiveData<LoadingResource<PhotoAlbum>> {
        return photoAlbumRepo.getPhotoAlbum()
    }
}