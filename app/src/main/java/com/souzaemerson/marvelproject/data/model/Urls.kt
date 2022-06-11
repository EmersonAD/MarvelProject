package com.souzaemerson.marvelproject.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Urls(
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String
): Serializable