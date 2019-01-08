package com.demo.chris.newscorpdemo.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.chris.newscorpdemo.R
import com.demo.chris.newscorpdemo.data.photos.AlbumPhoto
import com.demo.chris.newscorpdemo.data.photos.PhotoAlbum
import com.demo.chris.newscorpdemo.ui.views.ImageLoadingLayout
import kotlinx.android.synthetic.main.photos_item_layout.view.*
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
        return ImageHolder(LayoutInflater.from(context).inflate(R.layout.photos_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val photosForPos = photoAlbum.photoAlbumMap.values.toMutableList()[position]
        holder.bindData(photosForPos, onItemClickListener)
    }

    override fun onViewAttachedToWindow(holder: ImageHolder) {
        super.onViewAttachedToWindow(holder)

        // Load thumb execution is done in onViewAttachedToWindow
        // to ensure the holder's ImageView runnable has been created
        holder.loadPhotoThumb()
    }

    override fun onViewDetachedFromWindow(holder: ImageHolder) {
        super.onViewDetachedFromWindow(holder)
        // TODO :: Clean up resources....
    }

    override fun getItemCount(): Int {
        return photoAlbum.photoAlbumMap.size
    }

    class ImageHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private lateinit var albumPhoto: AlbumPhoto

        private val photoItemRoot: ViewGroup = itemView.photo_item_root
        private val albumIdText: TextView = itemView.photo_item_album_id_tv
        private val titleText: TextView = itemView.photo_item_title_tv
        private val photoIdText: TextView = itemView.photo_item_id_tv
        private val imageUrlText: TextView = itemView.photo_item_image_url_tv
        private val imageLoadingLayout: ImageLoadingLayout = itemView.photo_image_loading_layout

        private var externalPhotoItemClickListener: OnItemClickListener? = null

        private var photoItemClickListener = View.OnClickListener {
            Timber.d("Clicked photo with ID %s", albumPhoto.id.toString())
            externalPhotoItemClickListener?.onItemClick(albumPhoto)
        }

        fun loadPhotoThumb() {
            imageLoadingLayout.loadNetworkImage(albumPhoto.url)
        }

        fun bindData(albumPhoto: AlbumPhoto, albumPhotoClickListener: OnItemClickListener?) {
            // Set the data associated with ImageHolder instance
            this.albumPhoto = albumPhoto

            setTexts(albumPhoto)

            // Set the click listener on the ImageHolder root only after the data
            // has been retrieved. This prevents potential issues with clicking an
            // ImageView before its data has been retrieved.
            photoItemRoot.setOnClickListener(photoItemClickListener)

            // Assign any external click listener provided to the adapter instance
            externalPhotoItemClickListener = albumPhotoClickListener
        }

        private fun setTexts(albumPhoto: AlbumPhoto) {
            albumIdText.text = albumPhoto.albumId.toString()
            titleText.text = albumPhoto.title
            photoIdText.text = albumPhoto.id.toString()
            imageUrlText.text = albumPhoto.url
        }
    }
}