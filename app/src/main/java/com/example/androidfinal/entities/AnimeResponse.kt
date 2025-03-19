package com.example.androidfinal.entities

import com.google.gson.annotations.SerializedName

data class AnimeResponse(
    @SerializedName("data") val animeList: List<Anime>
)

data class Anime(
    @SerializedName("title") val title: String,
    @SerializedName("episodes") val episodes: Int?
)

data class EpisodeInfo(
    val title: String,
    val epNum: Int
)
