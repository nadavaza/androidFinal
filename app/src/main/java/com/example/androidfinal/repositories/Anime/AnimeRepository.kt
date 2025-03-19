package com.example.androidfinal.repositories.Anime

import com.example.androidfinal.entities.AnimeResponse
import com.example.androidfinal.entities.EpisodeInfo
import com.example.androidfinal.services.AnimeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val BASE_URL = "https://api.jikan.moe/v4/"

class AnimeRepository {

    private val animeService: AnimeService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        animeService = retrofit.create(AnimeService::class.java)
    }

    fun fetchAnimeList(callback: (List<EpisodeInfo>) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(AnimeService::class.java)

        val call = apiService.getAnimeList()
        call.enqueue(object : Callback<AnimeResponse> {
            override fun onResponse(call: Call<AnimeResponse>, response: Response<AnimeResponse>) {
                if (response.isSuccessful) {
                    val animeList = response.body()?.animeList ?: emptyList()
                    val episodeList = animeList.flatMap { anime ->
                        val episodes = anime.episodes ?: 0
                        List(episodes) { epNum ->
                            EpisodeInfo(
                                title = anime.title,
                                epNum = epNum + 1
                            )
                        }
                    }
                    callback(episodeList)
                } else {
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<AnimeResponse>, t: Throwable) {
                callback(emptyList())
            }
        })
    }
}