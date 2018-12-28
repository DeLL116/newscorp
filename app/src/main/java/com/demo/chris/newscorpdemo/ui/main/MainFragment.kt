package com.demo.chris.newscorpdemo.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.chris.newscorpdemo.NewsCorpDemoApplication
import com.demo.chris.newscorpdemo.R
import com.demo.chris.newscorpdemo.data.photos.WSPhotos
import com.demo.chris.newscorpdemo.ui.adapters.PhotosItemAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

        rv_list_photos.layoutManager = LinearLayoutManager(activity)

        val postsApi = NewsCorpDemoApplication.instance.retroNetWorker.retrofit.create(WSPhotos::class.java)

        val response = postsApi.getPhotos()

        response.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(IoScheduler())
            .subscribe {
                rv_list_photos.adapter = PhotosItemAdapter(it, this.context)
            }

    }

}
