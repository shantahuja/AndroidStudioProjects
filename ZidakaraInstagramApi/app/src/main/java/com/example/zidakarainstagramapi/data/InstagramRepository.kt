package com.example.zidakarainstagramapi.data

import com.example.zidakarainstagramapi.data.instagram.MediaData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object InstagramRepository {

    private const val BASE_URL = "https://graph.instagram.com"

    private val api: Api

    suspend fun getContent(): List<Content> {
        val result = api.getInstagramEdges().data
        val mediaDataList: MutableList<Content> = mutableListOf()
        edgeLoop@ for (edge in result) {
            val mediaData = api.getMediaData(edge.id)
            when (mediaData.media_type) {
                "CAROUSEL_ALBUM" -> {
                    val albumMediaData = api.getAlbumResponse(edge.id).data
                    mediaDataList.addAll(albumMediaData.map {
                        Content(it, edge.caption)
                    })
                }
                "VIDEO" -> {
                    mediaDataList.add(Content(mediaData, edge.caption))
                }
                else -> continue@edgeLoop
            }
        }
        return mediaDataList.filter { it.media_type == "VIDEO" }
    }

    init {
        val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            val request = chain.request()
            val url = request.url().newBuilder().addQueryParameter(
                "access_token",
                "IGQVJXLXlFMWw5WDNpb1hxYlpULTg1ZAkNVVEtwcm5RUkpKUnNOb050ZA1RwZAG80Mjh0T3BSWHo1Ujc3b2ZASdGgwRlJPWk5pWkFOOVhCOVQ2WG9qZAmJsNXBmdnQ5SV9sWVJ3Y1JJbElR"
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