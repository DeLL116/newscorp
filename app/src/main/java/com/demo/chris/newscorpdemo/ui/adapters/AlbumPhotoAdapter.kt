package com.demo.chris.newscorpdemo.ui.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.demo.chris.newscorpdemo.R
import com.demo.chris.newscorpdemo.data.photo.AlbumPhoto
import com.nochino.support.androidui.views.recyclerview.BaseRecyclerViewClickListener
import com.nochino.support.androidui.views.recyclerview.BaseViewHolder
import com.nochino.support.androidui.views.recyclerview.adapters.BaseRecyclerViewAdapter
import com.nochino.support.androidui.views.ImageLoadingLayout
import com.nochino.support.androidui.views.SwappableImageCardView
import kotlinx.android.synthetic.main.recycler_view_card_item_layout.view.*

class AlbumPhotoAdapter(context: Context)
    : BaseRecyclerViewAdapter<AlbumPhoto, BaseRecyclerViewClickListener<AlbumPhoto>, AlbumPhotoViewHolder>
    (context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumPhotoViewHolder {
        return AlbumPhotoViewHolder(inflate(R.layout.recycler_view_card_item_layout, parent))
    }

    override fun onViewRecycled(holder: AlbumPhotoViewHolder) {
        super.onViewRecycled(holder)
        // When the ViewHolder is recycled the ImageView in the holder is reset
        // so previously loaded images aren't visible during fast list flinging

        // Note...not using Kotlin synthetic view because of kotlin bug with library projects
        // TODO :: Bug --> Use Kotlin Synthetic View https://issuetracker.google.com/issues/78547457
        holder.itemView.findViewById<ImageLoadingLayout>(R.id.card_image_loading_layout).resetNetworkImage()
    }
}

class AlbumPhotoViewHolder(itemView: View)
    : BaseViewHolder<AlbumPhoto, BaseRecyclerViewClickListener<AlbumPhoto>>
    (itemView) {

    private val swappableImageCardView: SwappableImageCardView = itemView.rv_card_view

    override fun onBind(item: AlbumPhoto, listener: BaseRecyclerViewClickListener<AlbumPhoto>?) {
        swappableImageCardView.setOnClickListener{ listener?.onItemClicked(item) }
        swappableImageCardView.loadNetworkImage(item.getThumbnailPathSegmentIdentifier())
        swappableImageCardView.setData(item.title, item.albumId.toString(), item.id.toString(), item.thumbnailUrl)
    }
}