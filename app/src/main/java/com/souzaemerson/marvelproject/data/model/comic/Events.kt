package com.souzaemerson.marvelproject.data.model.comic

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)