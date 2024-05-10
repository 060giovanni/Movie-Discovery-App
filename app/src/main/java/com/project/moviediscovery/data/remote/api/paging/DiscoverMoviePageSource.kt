package com.project.moviediscovery.data.remote.api.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.project.moviediscovery.data.models.MovieItem
import com.project.moviediscovery.data.remote.api.ApiService
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException


class DiscoverMoviePageSource(private val apiService: ApiService) : PagingSource<Int, MovieItem>() {
    private val defaultPageIndex = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        val page = params.key ?: defaultPageIndex
        return try {
            val response = apiService.getDiscoverMovie(page)
            LoadResult.Page(
                response.results,
                prevKey = if (page == defaultPageIndex) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}