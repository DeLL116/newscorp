package com.demo.chris.newscorpdemo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.chris.newscorpdemo.R
import com.demo.chris.newscorpdemo.data.photo.AlbumPhoto
import com.demo.chris.newscorpdemo.data.photo.PhotoAlbum
import com.nochino.support.androidui.fragments.BaseObserverFragment
import com.nochino.support.networking.vo.LoadingResource
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * Fragment that is an observer of [PhotoAlbum] that can display a single [AlbumPhoto].
 * The [AlbumPhoto] data object can be provided directly to the fragment's bundle args,
 * or the id of an [AlbumPhoto] can be provided. In the latter, when this fragment is provided
 *  the [PhotoAlbum], the associated [AlbumPhoto] will be displayed.
 *
 * TODO :: Write test for when a [PhotoAlbum] is request, but the provided ID of an [AlbumPhoto] is not found in the returned data set
 */
class DetailFragment : BaseObserverFragment<PhotoAlbum, PhotoAlbumViewModel>() {

    override val loadingResourceViewModelClass: Class<PhotoAlbumViewModel>?
        get() = PhotoAlbumViewModel::class.java

    private var photoKeyId: String? = null
    private var albumPhoto: AlbumPhoto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Check if an ID has been provided
            photoKeyId = it.getString(ARG_ALBUM_PHOTO_ID_KEY)

            // Check if an AlbumPhoto object has been provided
            albumPhoto = it.getParcelable(ARG_ALBUM_PHOTO_KEY) as? AlbumPhoto


            //TODO :: Test if both are provided, and that both ID's match!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Null-check here because an AlbumPhoto object can be provided to
        // an instance of this fragment...in which case we can show it immediately
        // (but still observe the ViewModel for changes)
        if (albumPhoto != null) {
            onAlbumPhotoRetrieved(albumPhoto)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.title = albumPhoto?.title
    }

    override fun showSuccess(loadingResource: LoadingResource<PhotoAlbum>) {
        onAlbumPhotoRetrieved(loadingResource.data?.photoAlbumMap?.get(photoKeyId?.toInt()))
    }

    private fun onAlbumPhotoRetrieved(albumPhoto: AlbumPhoto?) {
        albumPhoto?.let {
            setAlbumPhoto(it)
        }
    }

    private fun setAlbumPhoto(albumPhoto: AlbumPhoto) {

        this.albumPhoto = albumPhoto

        swappableImageCardView.loadNetworkImage(albumPhoto.url)
        swappableImageCardView.setData(
            albumPhoto.title,
            albumPhoto.albumId.toString(),
            albumPhoto.id.toString(),
            albumPhoto.url
        )

        // Set texts (action bar title...etc)
        activity?.title = albumPhoto.title
    }

    @Suppress("unused")
    companion object {

        /**
         * Fragment bundle arg key to an AlbumPhoto object
         */
        private const val ARG_ALBUM_PHOTO_KEY = "albumPhotoKey"

        /**
         * Fragment bundle arg key to the ID of an AlbumPhoto
         * (which can be retrieved from a [PhotoAlbumViewModel] data source)
         */
        const val ARG_ALBUM_PHOTO_ID_KEY = "albumPhotoIdKey"

        /**
         * Builds a bundle which provides this fragment with
         * an [AlbumPhoto]
         */
        fun buildBundle(albumPhoto: AlbumPhoto): Bundle {
            return Bundle().apply {
                putParcelable(ARG_ALBUM_PHOTO_KEY, albumPhoto)
            }
        }

        // *** NOTICE HOW ABOVE BUNDLE BUILDING IS DIFFERENT THAN BELOW BUNDLE BUILDING ***

        /**
         * Builds a bundle which provides this fragment with
         * the ID (key) to an [AlbumPhoto] object.
         */
        @JvmStatic
        fun buildBundle(albumPhotoIdKey: String) =
            Bundle().apply {
                putString(ARG_ALBUM_PHOTO_ID_KEY, albumPhotoIdKey)
            }

        // TODO: Doc
        @JvmStatic
        fun newInstance(albumPhotoKey: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ALBUM_PHOTO_KEY, albumPhotoKey)
                }
            }

        // TODO: Doc
        @JvmStatic
        fun newInstance(albumPhoto: AlbumPhoto) =
                DetailFragment().apply {
                    arguments = buildBundle(albumPhoto)
                }
    }
}
