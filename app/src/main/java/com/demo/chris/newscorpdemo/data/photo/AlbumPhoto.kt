package com.demo.chris.newscorpdemo.data.photo

import android.net.Uri
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.nochino.support.androidui.views.PathSegmentModifier
import kotlinx.android.parcel.Parcelize

/**
 * Parcelable data model object class which describes a Photo
 *
 * @author Chris DelliSanti
 */
@Parcelize
data class AlbumPhoto (
    @SerializedName("albumId") val albumId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,

    // TODO :: url? Maybe use photoUrl?
    @SerializedName("url") val url: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String
) : Parcelable {

    fun getThumbnailPathSegmentIdentifier(): PathSegmentModifier {
        return PathSegmentModifier(
            thumbnailUrl,
            mapOf(PathSegmentModifier.widthPathSegment to Uri.parse(thumbnailUrl).pathSegments[0])
        )
    }

    fun getUrlPathSegmentIdentifier(): PathSegmentModifier {
        return PathSegmentModifier(url,
            mapOf(PathSegmentModifier.widthPathSegment to Uri.parse(url).pathSegments[0])
        )
    }
}