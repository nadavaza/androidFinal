package com.example.androidfinal.entities

import com.google.gson.annotations.SerializedName

data class AnimeResponse(
    @SerializedName("data") val animeList: List<Anime>
)

data class Anime(
    @SerializedName("title") val title: String,
    @SerializedName("episodes") val episodes: Int?,
    @SerializedName("images") val images: ImageWrapper
)

data class ImageWrapper(
    @SerializedName("jpg") val jpg: ImageUrl
)

data class ImageUrl(
    @SerializedName("image_url") val imageUrl: String
)

data class EpisodeInfo(
    val title: String,
    val epNum: Int,
    val photo: String
)
