package com.demo.chris.newscorpdemo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.demo.chris.newscorpdemo.R
import com.demo.chris.newscorpdemo.data.photos.AlbumPhoto
import com.demo.chris.newscorpdemo.data.photos.PhotoAlbum
import com.nochino.support.networking.vo.Resource
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * Fragment that can display an [AlbumPhoto]. The [AlbumPhoto] can be provided
 * directly to the fragment, or the id of an [AlbumPhoto] can be provided and
 * a request to retrieve a [PhotoAlbum] and parse the provided [AlbumPhoto]
 * from the [PhotoAlbum].
 *
 * TODO :: Write test for when a [PhotoAlbum] is request, but the provided ID of an [AlbumPhoto] is not found in the returned data set
 */
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


            //TODO :: Test if both are provided, and that both ID's match!
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

        // Create the PhotoAlbumViewModel containing the LiveData for the PhotoAlbum
        // retrieved from the network.
        photoAlbumViewModel = ViewModelProviders.of(requireActivity()).get(PhotoAlbumViewModel::class.java)

        // Null-check here because an AlbumPhoto object can be provided to
        // an instance of this fragment...in which case we can show it immediately
        // (but still observe the ViewModel for changes)
        if (albumPhoto != null) {
            onAlbumPhotoRetrieved(albumPhoto)
        } else {
            fetchPhotoAlbum()
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.title = albumPhoto?.title
    }

    private fun fetchPhotoAlbum() {
        // Set the observer on ViewModel's LiveData. This Observer will be notified
        // when the underlying data in the ViewModel has changed.
        photoAlbumViewModel.fetchData().observe(this, Observer<Resource<PhotoAlbum>> {
            // TODO :: Abstract on/handle all observable Resource states!
            val albumPhoto: AlbumPhoto? = it.data?.photoAlbumMap?.get(photoKeyId?.toInt())
            onAlbumPhotoRetrieved(albumPhoto)
        })
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

    // TODO :: Create abstraction
    private fun onLoadError() {

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
