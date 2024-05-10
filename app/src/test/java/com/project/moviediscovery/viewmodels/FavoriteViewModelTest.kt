package com.project.moviediscovery.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.project.moviediscovery.data.models.FavoritesEntity
import com.project.moviediscovery.data.repo.FavoriteRepository
import com.project.moviediscovery.ui.main.ui.favorite.FavoritesViewModel
import com.project.moviediscovery.utils.CoroutineTestRule
import com.project.moviediscovery.utils.LiveDataTestUtils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavoriteViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var favoriteRepository: FavoriteRepository


    private lateinit var favoritesViewModel: FavoritesViewModel

    private val dummyFavoritesEntity = FavoritesEntity(
        id = 1,
        title = "title",
        poster = "poster"
    )

    @Before
    fun setUp() {
        favoritesViewModel = FavoritesViewModel(favoriteRepository)
    }

    @Test
    fun `when Favorites Fetched Succesfully`() = runTest {
        val listFavoritesEntity = MutableLiveData(listOf(dummyFavoritesEntity))

        Mockito.`when`(favoriteRepository.getAllFavorites())
            .thenReturn(listFavoritesEntity)

        favoritesViewModel.getAllFavorite().getOrAwaitValue().let { result ->
            assertEquals(listFavoritesEntity.value,result)
        }

        Mockito.verify(favoriteRepository).getAllFavorites()
    }
}