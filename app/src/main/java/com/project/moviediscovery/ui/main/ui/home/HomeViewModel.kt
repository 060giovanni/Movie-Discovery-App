package com.project.moviediscovery.ui.main.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.moviediscovery.data.models.MovieItem
import com.project.moviediscovery.data.repo.MovieRepository

class HomeViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    val listDiscoverMovies: LiveData<PagingData<MovieItem>> by lazy {
        movieRepository.getDiscoverMovies().cachedIn(viewModelScope)
    }
}