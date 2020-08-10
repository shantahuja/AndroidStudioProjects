package com.example.zidakarainstagramapi.data

import com.example.zidakarainstagramapi.data.instagram.MediaData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object InstagramRepository {

    private const val BASE_URL = "https://graph.instagram.com"

    private val api: Api

    var content: List<Content>? = null

    suspend fun getContent(): List<Content> {
        if (content == null) {
            val result = api.getInstagramEdges().data
            val mediaDataList: MutableList<Content> = mutableListOf()
            edgeLoop@ for (edge in result) {
                val mediaData = api.getMediaData(edge.id)
                when (mediaData.media_type) {
                    "CAROUSEL_ALBUM" -> {
                        val albumMediaData =
                            api.getAlbumResponse(edge.id).data.filter { it.media_type == "VIDEO" }
                        mediaDataList.addAll(albumMediaData.mapIndexed { index, data ->
                            Content(data, edge.caption.split("\nx\n")[index])
                        })
                    }
                    "VIDEO" -> {
                        mediaDataList.add(Content(mediaData, edge.caption.split("\nx\n")[0]))
                    }
                    else -> continue@edgeLoop
                }
            }
            content = mediaDataList
            return mediaDataList
        } else {
            return content!!
        }
    }

    init {
        val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            val request = chain.request()
            val url = request.url().newBuilder().addQueryParameter(
                "access_token",
                "IGQVJYZAlk3YVU0OVFyMDZAjZA0FzWUExcWVTMUx2eUk3TlJodEpsRnFHWTR0UXBIYUVPbFhmdFl6LTVMeVdfSVNfX0hiUW9QRVNZAXzRtOFNTQkJDMk9kZAnpMNzFZAWlJlX295XzhtZAHV3"
            ).build()
            chain.proceed(request.newBuilder().url(url).build())
        }).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(Api::class.java)


    }
}