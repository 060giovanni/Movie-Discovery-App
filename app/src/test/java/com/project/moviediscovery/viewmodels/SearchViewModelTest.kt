package com.project.moviediscovery.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.project.moviediscovery.data.models.FavoritesEntity
import com.project.moviediscovery.data.models.MovieItem
import com.project.moviediscovery.data.models.MovieListResponse
import com.project.moviediscovery.data.repo.MovieRepository
import com.project.moviediscovery.ui.search.SearchViewModel
import com.project.moviediscovery.utils.CoroutineTestRule
import com.project.moviediscovery.utils.LiveDataTestUtils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import com.project.moviediscovery.utils.Result
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var movieRepository: MovieRepository


    private lateinit var searchViewModel: SearchViewModel

    private val dummyFavoritesEntity = FavoritesEntity(
        id = 1,
        title = "title",
        poster = "poster"
    )

    @Before
    fun setUp() {
        searchViewModel = SearchViewModel(movieRepository)
    }

    @Test
    fun `when User Search Succesfully Trigger`() = runTest {
        val dummyQuery = "query"
        val dummyMovieListResponse = MovieListResponse(results = arrayListOf(MovieItem(id = 1)))

        val expectedResponse = MutableLiveData<Result<MovieListResponse>>()
        expectedResponse.value =
            Result.Success(dummyMovieListResponse)

        Mockito.`when`(movieRepository.searchMovies(dummyQuery))
            .thenReturn(expectedResponse.asFlow())
        searchViewModel.searchMovies(dummyQuery)

        Mockito.verify(movieRepository).searchMovies(dummyQuery)
    }
}