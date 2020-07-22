package com.example.zidakarainstagramapi.data.instagram

import com.google.gson.annotations.SerializedName

data class Edge (
    @SerializedName("id") val id: Long,
    @SerializedName("caption") val caption: String
)