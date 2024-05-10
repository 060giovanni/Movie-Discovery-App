package com.project.moviediscovery.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.moviediscovery.data.models.MovieItem
import com.project.moviediscovery.utils.Result
import com.project.moviediscovery.data.repo.MovieRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchedMovies = MutableLiveData<ArrayList<MovieItem>>(arrayListOf())
    val searchedMovies: LiveData<ArrayList<MovieItem>> = _searchedMovies

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun searchMovies(query: String) {
        viewModelScope.launch {
            movieRepository.searchMovies(query).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _isLoading.value = true
                    }

                    is Result.Success -> {
                        _isLoading.value = false
                        _searchedMovies.value = result.data.results as ArrayList<MovieItem>
                    }

                    is Result.Error -> {
                        _isLoading.value = false
                        _message.value = result.error
                    }
                }
            }
        }
    }
}