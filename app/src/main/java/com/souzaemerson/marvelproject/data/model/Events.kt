package com.souzaemerson.marvelproject.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Events(
    @SerializedName("available")
    val available: Int,
    @SerializedName("collectionURI")
    val collectionURI: String,
    @SerializedName("items")
    val items: List<ItemsEvents>,
    @SerializedName("returned")
    val returned: Int
): Serializable
