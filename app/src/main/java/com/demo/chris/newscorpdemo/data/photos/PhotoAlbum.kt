package com.demo.chris.newscorpdemo.data.photos

/**
 * A Photo Album data model. It contains a map of [AlbumPhoto] object.
 *
 * @author Chris Delli Santi
 */
data class PhotoAlbum(var photosList: LinkedHashMap<Int, AlbumPhoto>)