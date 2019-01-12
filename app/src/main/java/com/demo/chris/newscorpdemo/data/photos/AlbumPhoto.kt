package com.demo.chris.newscorpdemo.data.photos

import android.net.Uri
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.nochino.support.androidui.views.PathSegmentModifier
import com.nochino.support.androidui.views.PathSegmentable
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
) : Parcelable, PathSegmentable {

    // *** [Begin] PathSegmentable Implementations ***
    override val pathSegmentsMap: Map<String, PathSegmentModifier>
        get() {
            return mapOf(
                thumbnailUrl to getThumbnailPathSegmentIdentifier(),
                url to getUrlPathSegmentIdentifier()
            )
        }

    override fun getPathSegmentModifier(pathString: String): PathSegmentModifier? {
        return pathSegmentsMap[pathString]
    }
    // *** [End] PathSegmentable Implementations ***

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