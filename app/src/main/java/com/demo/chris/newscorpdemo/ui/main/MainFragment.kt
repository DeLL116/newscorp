package com.demo.chris.newscorpdemo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.chris.newscorpdemo.R
import com.demo.chris.newscorpdemo.data.photos.AlbumPhoto
import com.demo.chris.newscorpdemo.data.photos.PhotoAlbum
import com.demo.chris.newscorpdemo.ui.adapters.AlbumPhotoAdapter
import com.google.android.material.appbar.AppBarLayout
import com.nochino.support.androidui.fragments.BaseFragment
import com.nochino.support.androidui.testing.CountingIdlingResourceViewModelFactory
import com.nochino.support.androidui.views.recyclerview.BaseRecyclerViewClickListener
import com.nochino.support.networking.vo.LoadingResource
import com.nochino.support.networking.vo.Status
import kotlinx.android.synthetic.main.main_fragment.*
import timber.log.Timber

class MainFragment : BaseFragment() {

    private lateinit var photoAlbumViewModel: PhotoAlbumViewModel

    private val adapterClickListener = object : BaseRecyclerViewClickListener<AlbumPhoto> {
        override fun onItemClicked(item: AlbumPhoto) {
            Timber.d("Clicked photo with ID %s", item.id.toString())

            // TODO :: Test opening DetailFragment with AlbumPhoto data object and with String ID of AlbumPhoto

            // Open DetailFragment by passing an AlbumPhoto object
            findNavController().navigate(R.id.detail_action, DetailFragment.buildBundle(item))

            // TODO :: Test providing an ID that does not exist in the data set
            // Open DetailFragment by passing the ID of an AlbumPhoto.
            // The PhotoAlbum will be retrieved and the AlbumPhoto of the ID will be displayed
//            findNavController().navigate(R.id.detail_action, DetailFragment.buildBundle(item.id.toString()))

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
        activity?.let {
            // TODO :: Move to "Staging Debug" Flavor class variant (don't keep in production code)!
            CountingIdlingResourceViewModelFactory.getActivityViewModel(it).decrementIdleResourceCounter()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Create the PhotoAlbumViewModel containing the LiveData for the PhotoAlbum
        // retrieved from the network.
        photoAlbumViewModel = ViewModelProviders.of(requireActivity()).get(PhotoAlbumViewModel::class.java)

        // Create the LayoutManager for the RecyclerView
        main_fragment_rv.layoutManager = LinearLayoutManager(activity)

        // Set the observer on ViewModel's LiveData. This Observer will be notified
        // when the underlying data in the ViewModel has changed.
        photoAlbumViewModel.fetchData().apply {
            // Whenever data is fetched...increment the idlingResource
            // TODO :: Move to "Staging Debug" Flavor class variant (don't keep in production code)!
            CountingIdlingResourceViewModelFactory.getFragmentViewModel(this@MainFragment)
                .incrementTestIdleResourceCounter()
        }.observe(this, Observer<LoadingResource<PhotoAlbum>> {

            // Get the status of the returned observed data
            // TODO :: Abstract on/handle all observable LoadingResource states!
            when (it.status) {
                Status.LOADING -> Toast.makeText(activity, "Loading", Toast.LENGTH_LONG).show()
                Status.SUCCESS -> it.data?.let { photoAlbum -> updateAdapter(photoAlbum) }
                Status.ERROR -> Toast.makeText(activity, "Error Loading", Toast.LENGTH_LONG).show()
            }

            // Whenever data is returned decrement the idlingResource
            // Should be null in production builds
            // For testing....decrements IdlingResource so Espresso
            // knows to proceed with testing.
            // See MainActivityTest.onRecyclerViewPopulated for incrementation
            // TODO :: Move to "Staging Debug" Flavor class variant (don't keep in production code)!
            CountingIdlingResourceViewModelFactory
                .getFragmentViewModel(this@MainFragment)
                .decrementIdleResourceCounter()
        })
    }

    override fun onResume() {
        super.onResume()

        // Reset the base title in the ToolBar whenever resuming this fragment
        activity?.title = getString(R.string.app_name)
    }

    private fun updateAdapter(photoAlbum: PhotoAlbum) {
        val adapterItems = photoAlbum.photoAlbumMap.values.toList()
        if (main_fragment_rv?.adapter == null) {
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
