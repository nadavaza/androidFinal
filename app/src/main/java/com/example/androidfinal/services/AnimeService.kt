package com.example.androidfinal.services


import com.example.androidfinal.entities.AnimeResponse
import retrofit2.Call
import retrofit2.http.GET

interface AnimeService {
    @GET("anime")
    fun getAnimeList(): Call<AnimeResponse>
}