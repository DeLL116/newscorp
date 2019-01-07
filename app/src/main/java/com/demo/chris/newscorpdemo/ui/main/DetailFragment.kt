package com.demo.chris.newscorpdemo.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.demo.chris.newscorpdemo.R
import com.demo.chris.newscorpdemo.data.photos.AlbumPhoto
import com.demo.chris.newscorpdemo.data.photos.PhotoAlbum
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {

    private lateinit var photoAlbumViewModel: PhotoAlbumViewModel

    private var photoKeyId: String? = null
    private var albumPhoto: AlbumPhoto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Check if an ID has been provided
            photoKeyId = it.getString(ARG_ALBUM_PHOTO_ID_KEY)

            // Check if an AlbumPhoto object has been provided
            albumPhoto = it.getParcelable(ARG_ALBUM_PHOTO_KEY) as? AlbumPhoto


            //TODO :: Test that both are provided and that both ID's match

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // AlbumPhoto object was provided to fragment
        // ...show immediately, but still observe for changes
        if (albumPhoto != null) {
            onAlbumPhotoRetrieved(albumPhoto)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Create the PhotoAlbumViewModel containing the LiveData for the PhotoAlbum
        // retrieved from the network.
        photoAlbumViewModel = ViewModelProviders.of(this.activity!!).get(PhotoAlbumViewModel::class.java)

        // Set the observer on ViewModel's LiveData. This Observer will be notified
        // when the underlying data in the ViewModel has changed.
        photoAlbumViewModel.fetchData().observe(this, Observer<PhotoAlbum> {
            val albumPhoto: AlbumPhoto? = it.photoAlbumMap[photoKeyId?.toInt()]
            onAlbumPhotoRetrieved(albumPhoto)
        })
    }

    override fun onResume() {
        super.onResume()
        activity?.title = albumPhoto?.title
    }

    private fun onAlbumPhotoRetrieved(albumPhoto: AlbumPhoto?) {
        if (albumPhoto != null) {
            setAlbumPhoto(albumPhoto)
        } else {
            onLoadError()
        }
    }

    private fun setAlbumPhoto(albumPhoto: AlbumPhoto) {

        this.albumPhoto = albumPhoto

        // Load the image from the network
        detail_image.loadNetworkImage(albumPhoto.url)

        // Set texts (action bar title...etc)
        activity?.title = albumPhoto.title
    }

    // TODO :: Create abstraction
    private fun onLoadError() {

    }

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
