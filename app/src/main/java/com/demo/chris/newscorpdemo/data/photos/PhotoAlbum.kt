package com.demo.chris.newscorpdemo.data.photos

/**
 * A Photo Album data model. It contains a Linked HashMap (for order retention) of [AlbumPhoto] object.
 *
 * @author Chris Delli Santi
 */
data class PhotoAlbum(var photoAlbumMap: LinkedHashMap<Int, AlbumPhoto>)