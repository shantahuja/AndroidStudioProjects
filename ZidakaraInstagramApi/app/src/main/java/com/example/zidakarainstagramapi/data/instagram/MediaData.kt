package com.example.zidakarainstagramapi.data.instagram

import com.google.gson.annotations.SerializedName

data class MediaData (
    @SerializedName("id") val id: Long,
    @SerializedName("media_type") val media_type: String,
    @SerializedName("media_url") val media_url: String
)