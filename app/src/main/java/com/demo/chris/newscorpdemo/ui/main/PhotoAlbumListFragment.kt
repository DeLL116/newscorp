package com.demo.chris.newscorpdemo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.demo.chris.newscorpdemo.R
import com.demo.chris.newscorpdemo.data.photo.AlbumPhoto
import com.demo.chris.newscorpdemo.data.photo.PhotoAlbum
import com.demo.chris.newscorpdemo.ui.adapters.AlbumPhotoAdapter
import com.nochino.support.androidui.fragments.BaseObserverFragment
import com.nochino.support.androidui.testing.CountingIdlingResourceViewModelFactory
import com.nochino.support.androidui.views.recyclerview.BaseRecyclerViewClickListener
import com.nochino.support.networking.vo.LoadingResource
import kotlinx.android.synthetic.main.fragment_photo_album_list.*
import timber.log.Timber

class PhotoAlbumListFragment
    : BaseObserverFragment<PhotoAlbum, PhotoAlbumViewModel>(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override val loadingResourceViewModelClass: Class<PhotoAlbumViewModel>?
        get() = PhotoAlbumViewModel::class.java

    private val adapterClickListener = object : BaseRecyclerViewClickListener<AlbumPhoto> {
        override fun onItemClicked(item: AlbumPhoto) {
            Timber.d("Clicked photo with ID %s", item.id.toString())

            // TODO :: Test opening DetailFragment with AlbumPhoto data object and with String ID of AlbumPhoto
            // TODO :: Test providing an ID that does not exist in the data set

            // Open DetailFragment by passing the Parcelable AlbumPhoto object to the Fragment Bundle
            // findNavController().navigate(R.id.detail_action, DetailFragment.buildBundle(item))

            // Open DetailFragment by passing the ID of an AlbumPhoto.
            // The PhotoAlbum will be retrieved and the AlbumPhoto of the ID will be displayed
            findNavController().navigate(R.id.detail_action, DetailFragment.buildBundle(item.id.toString()))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val fragLayout = inflater.inflate(R.layout.fragment_photo_album_list, container, false)

        // TODO :: Move "swipe to refresh" capabilities down to BaseObserverFragment
        swipeRefreshLayout = fragLayout.findViewById(R.id.photo_album_list_swipe_refresh_container)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )
        return fragLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            // TODO :: Move to "Staging Debug" Flavor class variant (don't keep in production code)!
            CountingIdlingResourceViewModelFactory.getActivityViewModel(it).decrementIdleResourceCounter()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Create the LayoutManager for the RecyclerView
        photo_album_list_fragment_rv.layoutManager = LinearLayoutManager(activity)
    }

    override fun onResume() {
        super.onResume()

        // Reset the base title in the ToolBar whenever resuming this fragment
        activity?.title = getString(R.string.app_name)
    }

    override fun showSuccess(loadingResource: LoadingResource<PhotoAlbum>) {
        super.showSuccess(loadingResource)

        // Stopping swipe refresh
        swipeRefreshLayout.isRefreshing = false

        // Post on the fragment's view to avoid UI hitching
        view?.post {
            // Update the adapter...non-null asserted call(!!) to data because
            // if "success" is invoked the PhotoAlbum data should exist!
            updateAdapter(loadingResource.data!!)
        }
    }

    private fun updateAdapter(photoAlbum: PhotoAlbum) {
        val adapterItems = photoAlbum.photoAlbumMap.values.toList()

        photo_album_list_fragment_rv?.let {
            if (photo_album_list_fragment_rv.adapter == null) {
                // Try to create Adapter the adapter + set the listener
                this.context?.let {
                    photo_album_list_fragment_rv.adapter = AlbumPhotoAdapter(it).apply {
                        setItems(adapterItems)
                        setListener(adapterClickListener)
                    }.also { albumPhotoAdapter ->
                        photo_album_list_fragment_rv.adapter = albumPhotoAdapter
                    }
                }
            } else {
                (photo_album_list_fragment_rv.adapter as AlbumPhotoAdapter?)?.setItems(adapterItems)
            }

            // TODO :: Move scheduling layout animation elsewhere
            // --> It shouldn't happen every time data is set on the adapter
            photo_album_list_fragment_rv.scheduleLayoutAnimation()

            // Ensure the AppBar's scroll flags are set to move out of the way
            // once the RecyclerView List has been populated
            setAppBarScrollFlags()
        }

        CountingIdlingResourceViewModelFactory.getFragmentViewModel(this).decrementIdleResourceCounter()
    }

    override fun onRefresh() {
        // Showing refresh animation before making http call
        swipeRefreshLayout.isRefreshing = true
        fetchAndObserve(true)
    }
}
