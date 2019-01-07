package com.demo.chris.newscorpdemo.data.photos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Parcelable data model object class which describes a Photo in a Photo Album
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
) : Parcelable