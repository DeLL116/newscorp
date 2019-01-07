package com.demo.chris.newscorpdemo.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.demo.chris.newscorpdemo.R
import com.demo.chris.newscorpdemo.data.photos.AlbumPhoto
import com.demo.chris.newscorpdemo.data.photos.PhotoAlbum
import com.demo.chris.newscorpdemo.ui.adapters.PhotoAlbumAdapter
import kotlinx.android.synthetic.main.main_fragment.*
import timber.log.Timber

class MainFragment : Fragment() {

    private lateinit var photoAlbumViewModel: PhotoAlbumViewModel

    private val adapterClickListener = object : PhotoAlbumAdapter.OnItemClickListener {
        override fun onItemClick(albumPhoto: AlbumPhoto) {
            Timber.d("Clicked photo with ID %s", albumPhoto.id.toString())

            // TODO :: Test providing as data object and as string

//            findNavController().navigate(R.id.detail_action, DetailFragment.buildBundle(albumPhoto))
            findNavController().navigate(R.id.detail_action, DetailFragment.buildBundle(albumPhoto.id.toString()))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Create the PhotoAlbumViewModel containing the LiveData for the PhotoAlbum
        // retrieved from the network.
        photoAlbumViewModel = ViewModelProviders.of(requireActivity()).get(PhotoAlbumViewModel::class.java)

        // Create the LayoutManager for the RecyclerView
        rv_list_photos.layoutManager = LinearLayoutManager(activity)

        // Set the observer on ViewModel's LiveData. This Observer will be notified
        // when the underlying data in the ViewModel has changed.
        photoAlbumViewModel.fetchData().observe(this, Observer<PhotoAlbum> {
            updateAdapter(it)
        })
    }

    override fun onResume() {
        super.onResume()

        // Reset the base title in the ToolBar whenever resuming this fragment
        activity?.title = getString(R.string.app_name)
    }

    private fun updateAdapter(photoAlbum: PhotoAlbum) {
        if (rv_list_photos.adapter == null) {
            rv_list_photos.adapter = PhotoAlbumAdapter(
                photoAlbum,
                this.context,
                adapterClickListener
            )
        }
    }
}
