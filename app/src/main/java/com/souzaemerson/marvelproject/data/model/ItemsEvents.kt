package com.souzaemerson.marvelproject.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ItemsEvents(
    @SerializedName("resourceURI")
    val resourceURI: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("returned")
    val returned: Int
): Serializable
