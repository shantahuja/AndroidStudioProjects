package com.example.zidakarainstagramapi.data

import com.example.zidakarainstagramapi.data.instagram.AlbumResponse
import com.example.zidakarainstagramapi.data.instagram.EdgeResponse
import com.example.zidakarainstagramapi.data.instagram.MediaData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("me/media") suspend fun getInstagramEdges(
        @Query("fields") fields: String = "id,caption"
    ): EdgeResponse

    @GET("{id}") suspend fun getMediaData(
        @Path("id") id: Long,
        @Query("fields") fields: String = "id,media_type,media_url"
    ): MediaData

    @GET("{id}/children") suspend fun getAlbumResponse(
        @Path("id") id: Long,
        @Query("fields") fields: String = "id,media_type,media_url"
    ): AlbumResponse
}