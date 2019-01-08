package com.demo.chris.newscorpdemo.ui.views

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class NetworkImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    /**
     * Loads an image from the network with Picasso defaults and Picasso FIT
     */
    fun loadNetworkImageWithFit(imageUrl: String?) {
        Picasso.get().load(imageUrl).fit().into(this)
    }

    /**
     * Loads an image from the network with Picasso defaults
     */
    fun loadNetworkImage(imageUrl: String?) {
        Picasso.get().load(imageUrl).into(this)
    }

    /**
     * Loads an image from the network with Picasso using pixel-perfect width
     */
    fun loadPixelPerfectNetworkImage(imageUrl: String, target: Target, width: Int) {
        val dimenPathSegment = Uri.parse(imageUrl).pathSegments[0].toString()
        val pixelPerfectImageUrl = imageUrl.replace(dimenPathSegment, width.toString(), true)
        Picasso.get().load(pixelPerfectImageUrl).into(target)
    }
}