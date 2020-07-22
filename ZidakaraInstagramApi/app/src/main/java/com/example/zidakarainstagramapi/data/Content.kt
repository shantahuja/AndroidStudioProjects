package com.example.zidakarainstagramapi.data

import com.example.zidakarainstagramapi.data.instagram.MediaData
import com.google.gson.annotations.SerializedName

data class Content(
    val id: Long,
    val media_type: String,
    val media_url: String,
    val caption: String
) {
    constructor(mediaData: MediaData, caption: String) : this(mediaData.id, mediaData.media_type, mediaData.media_url, caption)
}