package com.souzaemerson.marvelproject.data.model.comic

data class Thumbnail(
    val extension: String,
    val path: String,
    val width: Int,
    val height: Int
){
    val aspectRatio: Float get() = 1f
}