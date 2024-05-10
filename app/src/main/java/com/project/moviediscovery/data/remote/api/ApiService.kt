package com.project.moviediscovery.data.remote.api

import com.project.moviediscovery.BuildConfig
import com.project.moviediscovery.data.models.DetailResponse
import com.project.moviediscovery.data.models.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("3/discover/movie")
    suspend fun getDiscoverMovie(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_TOKEN
    ): MovieListResponse

    @GET("3/movie/{movieId}")
    suspend fun getMovieDetail(
        @Path("movieId") movieId: Int
    ): DetailResponse

    @GET("3/movie/{movieId}/similar")
    suspend fun getSimilarMovie(
        @Path("movieId") movieId: Int,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_TOKEN
    ): MovieListResponse

    @GET("3/search/movie")
    suspend fun searchMovies(@Query("query") query: String): MovieListResponse
}
