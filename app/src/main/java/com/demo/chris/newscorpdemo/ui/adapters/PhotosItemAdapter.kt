package com.demo.chris.newscorpdemo.ui.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import com.demo.chris.newscorpdemo.R
import com.demo.chris.newscorpdemo.data.photos.AlbumPhoto
import com.demo.chris.newscorpdemo.data.photos.PhotoAlbum
import com.demo.chris.newscorpdemo.ui.views.NetworkImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.photos_item_layout.view.*
import timber.log.Timber
import java.lang.Exception

class PhotosItemAdapter(private val photoAlbum: PhotoAlbum, val context: Context?, private val onItemClickListener: OnItemClickListener?)
    : RecyclerView.Adapter<PhotosItemAdapter.ImageHolder>() {

    interface OnItemClickListener {
        fun onItemClick(albumPhoto: AlbumPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosItemAdapter.ImageHolder {
        return ImageHolder(LayoutInflater.from(context).inflate(R.layout.photos_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val photosForPos = photoAlbum.photosList[position]
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
        return photoAlbum.photosList.size
    }

    class ImageHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private lateinit var albumPhoto: AlbumPhoto

        private val photoItemRoot: ViewGroup = itemView.photo_item_root

        private var photoThumbImageView: NetworkImageView = itemView.photo_item_thumb
        private var photoProgressBar: ContentLoadingProgressBar = itemView.photo_item_progress

        private val albumIdText: TextView = itemView.photo_item_album_id_tv
        private val titleText: TextView = itemView.photo_item_title_tv
        private val photoIdText: TextView = itemView.photo_item_id_tv
        private val imageUrlText: TextView = itemView.photo_item_image_url_tv

        private var externalPhotoItemClickListener: OnItemClickListener? = null
        private var photoItemClickListener = View.OnClickListener {
            Timber.d("Clicked photo with ID %s", albumPhoto.id.toString())
            externalPhotoItemClickListener?.onItemClick(albumPhoto)
        }

        private val photoThumbTarget = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                photoProgressBar.visibility = View.VISIBLE
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                // Hide the ProgressBar *before* displaying the Bitmap
                photoProgressBar.visibility = View.INVISIBLE

                // Set the bitmap before making the ImageView visible
                photoThumbImageView.setImageBitmap(bitmap)
                photoThumbImageView.visibility = View.VISIBLE
            }
        }

        fun loadPhotoThumb() {
            // Set any current thumb ImageView to invisible to prevent any
            // potentially recycled ImageViews from showing older / recycled images
            photoThumbImageView.visibility = View.INVISIBLE

            // Post a runnable on the ImageView's handler to load the network image.
            // This is done to ensure the width and height of the rendered ImageView
            // can be derived before requesting a pixel-perfect image from the
            // network resource.
            photoThumbImageView.handler.post {
                photoThumbImageView.loadPixelPerfectNetworkImage(
                    albumPhoto.thumbnailUrl,
                    photoThumbTarget,
                    photoThumbImageView.width,
                    photoThumbImageView.height
                )
            }
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