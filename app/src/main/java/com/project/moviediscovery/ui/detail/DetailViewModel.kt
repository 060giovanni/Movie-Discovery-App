package com.project.moviediscovery.ui.detail

import android.icu.text.CaseMap.Title
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.moviediscovery.data.models.DetailResponse
import com.project.moviediscovery.data.models.FavoritesEntity
import com.project.moviediscovery.data.models.MovieItem
import com.project.moviediscovery.data.repo.FavoriteRepository
import com.project.moviediscovery.data.repo.MovieRepository
import com.project.moviediscovery.utils.Result
import kotlinx.coroutines.launch

class DetailViewModel(
    private val movieId: Int,
    private val movieRepository: MovieRepository,
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailMovie = MutableLiveData<DetailResponse>()
    val detailMovie: LiveData<DetailResponse> = _detailMovie

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    val listSimilarMovies: LiveData<PagingData<MovieItem>> by lazy {
        movieRepository.getSimilarMovies(movieId).cachedIn(viewModelScope)
    }

    init {
        getMovieDetail()
    }

    private fun getMovieDetail() {
        viewModelScope.launch {
            movieRepository.getMovieDetail(movieId).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _isLoading.postValue(true)
                    }

                    is Result.Success -> {
                        _isLoading.postValue(false)
                        _detailMovie.postValue(result.data)

                        viewModelScope.launch {
                            checkFavorite()
                        }
                    }

                    is Result.Error -> {
                        _isLoading.postValue(false)
                        _message.postValue(result.error)
                    }
                }
            }
        }
    }

    fun insertFavorite(poster: String, title: String) {
        favoriteRepository.addFavorite(
            FavoritesEntity(
                id = movieId,
                poster = poster,
                title = title
            )
        ).apply {
            viewModelScope.launch {
                checkFavorite()
            }
        }
    }

    fun deleteFavorite() {
        favoriteRepository.removeFavorite(movieId).apply {
            viewModelScope.launch {
                checkFavorite()
            }
        }
    }

    private suspend fun checkFavorite() {
        favoriteRepository.checkFavoriteMovie(movieId).asFlow().collect {
            _isFavorite.postValue(it.isNotEmpty())
        }
    }
}