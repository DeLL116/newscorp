package com.demo.chris.newscorpdemo.ui.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.core.widget.ContentLoadingProgressBar
import com.demo.chris.newscorpdemo.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception

/**
 * Custom [RelativeLayout] that contains a [NetworkImageView] and a [ContentLoadingProgressBar]
 * to display / dismiss the [ContentLoadingProgressBar] when an image is being requested
 * from a network resource.
 */
class ImageLoadingLayout : RelativeLayout {

    private lateinit var networkImageView: NetworkImageView
    private lateinit var loadingProgressBar: ContentLoadingProgressBar

    constructor(context: Context) : super(context) { init(null, 0) }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init(attrs, 0) }
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {

        View.inflate(context, R.layout.image_loading_layout, this)

        // Load XML defined attributes
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageLoadingLayout, defStyle, 0)

        // ---> Init any XML defined attributes here before recycle

        typedArray.recycle()

        networkImageView = findViewById(R.id.network_image_view)
        loadingProgressBar = findViewById(R.id.loading_progress_indeterminate)
    }

    private val photoThumbTarget = object : Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            loadingProgressBar.visibility = View.VISIBLE
        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            // TODO :: Define drawable failed bitmap
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {

            loadingProgressBar.visibility = View.INVISIBLE

            // Set the bitmap before making the ImageView visible with animation
            networkImageView.setImageBitmap(bitmap)
            networkImageView.visibility = View.VISIBLE
            // Animate the content view to 100% opacity, and clear any animation
            // listener set on the view.
            networkImageView.animate().alpha(1f)
                .setDuration(200) // TODO :: Allow XML attribute setting for duration
                .start()
        }
    }

    fun loadNetworkImage(networkImageUrl: String) {

        // Set any current thumb ImageView to invisible to prevent any
        // potentially recycled ImageViews from showing older / recycled images
        (View.VISIBLE == networkImageView.visibility).let {
            networkImageView.visibility = View.INVISIBLE
        }

        (networkImageView.alpha > 0.0f).let {
            networkImageView.alpha = 0.0f
        }

        // Post a runnable on the ImageView's handler (to load the network
        // image) to ensure the width and height of the rendered ImageView
        // can be derived before requesting a pixel-perfect image from the
        // network resource.
        networkImageView.handler.post {
            networkImageView.loadPixelPerfectNetworkImage(
                networkImageUrl,
                photoThumbTarget,
                networkImageView.width
            )
        }
    }
}
