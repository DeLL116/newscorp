package com.demo.chris.newscorpdemo.data.photos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Parcelable data model object class which describes a Photo Album.
 *
 * It contains a Linked HashMap (for order retention) of [AlbumPhoto] objects.
 * @author Chris DelliSanti
 */
@Parcelize
data class PhotoAlbum(var photoAlbumMap: LinkedHashMap<Int, AlbumPhoto>
) : Parcelable