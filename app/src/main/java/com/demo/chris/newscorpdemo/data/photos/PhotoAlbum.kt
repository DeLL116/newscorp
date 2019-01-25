package com.demo.chris.newscorpdemo.data.photos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Parcelable data model object class which describes a Photo Album.
 *
 * It contains a Map of [AlbumPhoto] objects. If order is important use a
 * [LinkedHashMap] as the map instance.
 *
 * @param photoAlbumMap A map of [AlbumPhoto] objects where the key of each object
 * is the ID of the [AlbumPhoto].
 *
 * @author Chris DelliSanti
 */
@Parcelize
data class PhotoAlbum(var photoAlbumMap: Map<Int, AlbumPhoto>) : Parcelable