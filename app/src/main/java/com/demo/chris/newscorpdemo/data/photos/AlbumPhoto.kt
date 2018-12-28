package com.demo.chris.newscorpdemo.data.photos

import com.google.gson.annotations.SerializedName

data class AlbumPhoto (
    @SerializedName("albumId") val albumId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,

    // TODO :: url? Maybe use photoUrl?
    @SerializedName("url") val url: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String
)