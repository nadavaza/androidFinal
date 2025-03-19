package com.example.androidfinal.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidfinal.entities.EpisodeInfo
import com.example.androidfinal.repositories.Anime.AnimeRepository

class AnimeViewModel(application: Application) : AndroidViewModel(application) {

    private val animeRepository = AnimeRepository()

    private val _episodes = MutableLiveData<List<EpisodeInfo>>()
    val episodes: LiveData<List<EpisodeInfo>> get() = _episodes

    init {
        fetchAnimeEpisodes()
    }

    private fun fetchAnimeEpisodes() {
        animeRepository.fetchAnimeList { episodeList ->
            _episodes.postValue(episodeList)
        }
    }

    fun getImageForTitle(title: String): String? {
        println(_episodes.value)
        println(title)
        return _episodes.value?.firstOrNull { it.title == title }?.photo
    }
}