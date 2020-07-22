package com.example.zidakarainstagramapi.data.instagram

import com.google.gson.annotations.SerializedName

data class AlbumResponse (
    @SerializedName("data") val data: List<MediaData>
)