package com.demo.chris.newscorpdemo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.chris.newscorpdemo.R
import com.demo.chris.newscorpdemo.data.photos.AlbumPhoto
import com.demo.chris.newscorpdemo.data.photos.PhotoAlbum
import com.demo.chris.newscorpdemo.ui.adapters.AlbumPhotoAdapter
import com.google.android.material.appbar.AppBarLayout
import com.nochino.support.androidui.fragments.BaseObserverFragment
import com.nochino.support.androidui.testing.CountingIdlingResourceViewModelFactory
import com.nochino.support.androidui.views.recyclerview.BaseRecyclerViewClickListener
import com.nochino.support.networking.vo.LoadingResource
import kotlinx.android.synthetic.main.main_fragment.*
import timber.log.Timber

class MainFragment : BaseObserverFragment<PhotoAlbum, PhotoAlbumViewModel>() {

    override val loadingResourceViewModelClass: Class<PhotoAlbumViewModel>?
        get() = PhotoAlbumViewModel::class.java

    private val adapterClickListener = object : BaseRecyclerViewClickListener<AlbumPhoto> {
        override fun onItemClicked(item: AlbumPhoto) {
            Timber.d("Clicked photo with ID %s", item.id.toString())

            // TODO :: Test opening DetailFragment with AlbumPhoto data object and with String ID of AlbumPhoto

            // Open DetailFragment by passing an AlbumPhoto object
//            findNavController().navigate(R.id.detail_action, DetailFragment.buildBundle(item))

            // TODO :: Test providing an ID that does not exist in the data set
            // Open DetailFragment by passing the ID of an AlbumPhoto.
            // The PhotoAlbum will be retrieved and the AlbumPhoto of the ID will be displayed
            findNavController().navigate(R.id.detail_action, DetailFragment.buildBundle(item.id.toString()))

            // TODO :: Extract logic to expand the toolbar when leaving a fragment
            // TODO :: Bug --> Use Kotlin Synthetic View https://issuetracker.google.com/issues/78547457
            activity?.findViewById<AppBarLayout>(R.id.base_app_bar)?.setExpanded(true, true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAppBarScrollFlags()

        activity?.let {
            // TODO :: Move to "Staging Debug" Flavor class variant (don't keep in production code)!
            CountingIdlingResourceViewModelFactory.getActivityViewModel(it).decrementIdleResourceCounter()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Create the LayoutManager for the RecyclerView
        main_fragment_rv.layoutManager = LinearLayoutManager(activity)
    }

    override fun onResume() {
        super.onResume()

        // Reset the base title in the ToolBar whenever resuming this fragment
        activity?.title = getString(R.string.app_name)
    }

    override fun showSuccess(loadingResource: LoadingResource<PhotoAlbum>) {
        super.showSuccess(loadingResource)

        // Post on the fragment's view to avoid jank
        view?.post {
            // Update the adapter...non-null asserted call(!!) to data because
            // if "success" is invoked the PhotoAlbum data should exist!
            updateAdapter(loadingResource.data!!)
        }
    }

    private fun updateAdapter(photoAlbum: PhotoAlbum) {
        val adapterItems = photoAlbum.photoAlbumMap.values.toList()

        main_fragment_rv?.let {
            if (main_fragment_rv.adapter == null) {
                // Try to create Adapter the adapter + set the listener
                this.context?.let {
                    main_fragment_rv.adapter = AlbumPhotoAdapter(it).apply {
                        setItems(adapterItems)
                        setListener(adapterClickListener)
                    }.also { albumPhotoAdapter ->
                        main_fragment_rv.adapter = albumPhotoAdapter
                    }
                }
            } else {
                (main_fragment_rv.adapter as AlbumPhotoAdapter?)?.setItems(adapterItems)
            }

            // TODO :: Move scheduling layout animation elsewhere
            // --> It shouldn't happen every time data is set on the adapter
            main_fragment_rv.scheduleLayoutAnimation()
        }
    }
}
