package com.project.moviediscovery.data.repo

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.project.moviediscovery.data.models.MovieItem
import com.project.moviediscovery.data.remote.api.ApiService
import com.project.moviediscovery.data.remote.api.paging.DiscoverMoviePageSource
import com.project.moviediscovery.data.remote.api.paging.SimilarMoviePageSource
import com.project.moviediscovery.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(
    private val apiService: ApiService
) {
    fun getDiscoverMovies(): LiveData<PagingData<MovieItem>> {
        return Pager(config = PagingConfig(10, enablePlaceholders = false)) {
            DiscoverMoviePageSource(apiService)
        }.liveData
    }

    fun getSimilarMovies(movieId: Int): LiveData<PagingData<MovieItem>> {
        return Pager(config = PagingConfig(10, enablePlaceholders = false)) {
            SimilarMoviePageSource(apiService, movieId)
        }.liveData
    }

    fun getMovieDetail(movieId: Int) = flow {
        emit(Result.Loading)
        try {
            val detailResponse = apiService.getMovieDetail(movieId)
            emit(Result.Success(detailResponse))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun searchMovies(query: String) = flow {
        emit(Result.Loading)
        try {
            val searchedUsers = apiService.searchMovies(query)
            emit(Result.Success(searchedUsers))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}