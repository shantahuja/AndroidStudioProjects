package com.example.zidakarainstagramapi.data.instagram

import com.example.zidakarainstagramapi.data.instagram.Edge
import com.google.gson.annotations.SerializedName

data class EdgeResponse (
    @SerializedName("data") val data: List<Edge>
)
