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

    private var photoKey: String? = null
    private var albumPhoto: AlbumPhoto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photoKey = it.getString(ARG_ALBUM_PHOTO_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Create the PhotoAlbumViewModel containing the LiveData for the PhotoAlbum
        // retrieved from the network.
        photoAlbumViewModel = ViewModelProviders.of(this.activity!!).get(PhotoAlbumViewModel::class.java)
        // Set the observer on ViewModel's LiveData. This Observer will be notified
        // when the underlying data in the ViewModel has changed.
        photoAlbumViewModel.fetchData().observe(this, Observer<PhotoAlbum> {
            val albumPhoto: AlbumPhoto? = it.photosList[photoKey?.toInt()]
            loadAlbumPhoto(albumPhoto)
        })
    }

    override fun onResume() {
        super.onResume()
        activity?.title = albumPhoto?.title
    }

    private fun loadAlbumPhoto(albumPhoto: AlbumPhoto?) {
        this.albumPhoto = albumPhoto

        // Load the image from the network
        detail_image.loadNetworkImage(albumPhoto?.url)

        // Set texts (action bar title...etc)
        activity?.title = albumPhoto?.title
    }

    companion object {
        const val ARG_ALBUM_PHOTO_KEY = "albumPhotoKey"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(albumPhotoKey: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ALBUM_PHOTO_KEY, albumPhotoKey)
                }
            }
    }
}
