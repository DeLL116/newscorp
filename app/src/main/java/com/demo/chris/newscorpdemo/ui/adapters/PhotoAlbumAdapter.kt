package com.demo.chris.newscorpdemo.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.chris.newscorpdemo.R
import com.demo.chris.newscorpdemo.data.photos.AlbumPhoto
import com.demo.chris.newscorpdemo.data.photos.PhotoAlbum
import com.nochino.support.androidui.views.ImageLoadingLayout
import com.nochino.support.androidui.views.SwappableImageCardView
import kotlinx.android.synthetic.main.recycler_view_card_item_layout.view.*
import timber.log.Timber

class PhotoAlbumAdapter(
    private val photoAlbum: PhotoAlbum,
    private val context: Context?,
    private val onItemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<PhotoAlbumAdapter.ImageHolder>() {

    interface OnItemClickListener {
        fun onItemClick(albumPhoto: AlbumPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoAlbumAdapter.ImageHolder {
        return ImageHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_card_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val albumPhotoForPos = photoAlbum.photoAlbumMap.values.toList()[position]
        holder.bindData(albumPhotoForPos, onItemClickListener)
    }

    override fun onViewRecycled(holder: ImageHolder) {
        super.onViewRecycled(holder)
        Timber.d("View Recycled")

        // When the ViewHolder is recycled reset the ImageView in
        // the holder so previously loaded images aren't visible
        // during fast list flinging
        // Note...not using Kotlin synthetic view because of kotlin bug with library projects
        // --->https://issuetracker.google.com/issues/78547457
        holder.itemView.findViewById<ImageLoadingLayout>(R.id.card_image_loading_layout).resetNetworkImage()
    }

    override fun getItemCount(): Int {
        return photoAlbum.photoAlbumMap.size
    }

    class ImageHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private lateinit var albumPhoto: AlbumPhoto
        private val swappableImageCardView: SwappableImageCardView = itemView.rv_card_view
        private var externalPhotoItemClickListener: OnItemClickListener? = null
        private var photoItemClickListener = View.OnClickListener {
            Timber.d("Clicked photo with ID %s", albumPhoto.id.toString())
            externalPhotoItemClickListener?.onItemClick(albumPhoto)
        }

        fun bindData(albumPhoto: AlbumPhoto, albumPhotoClickListener: OnItemClickListener?) {
            // Set the data associated with ImageHolder instance
            this.albumPhoto = albumPhoto

            swappableImageCardView.loadNetworkImage(
                albumPhoto.getPathSegmentModifier(albumPhoto.thumbnailUrl)
            )

            setTexts(albumPhoto)

            // Set the click listener on the ImageHolder root only after the data
            // has been retrieved. This prevents potential issues with clicking an
            // ImageView before its data has been retrieved.
            swappableImageCardView.setOnClickListener(photoItemClickListener)

            // Assign any external click listener provided to the adapter instance
            externalPhotoItemClickListener = albumPhotoClickListener
        }

        @Suppress("unused")
        private fun setImageUrl(networkImageUrl: String) {
            swappableImageCardView.loadNetworkImage(networkImageUrl)
        }

        private fun setTexts(albumPhoto: AlbumPhoto) {
            swappableImageCardView.setData(
                albumPhoto.title,
                albumPhoto.albumId.toString(),
                albumPhoto.id.toString(),
                albumPhoto.thumbnailUrl
            )
        }
    }
}