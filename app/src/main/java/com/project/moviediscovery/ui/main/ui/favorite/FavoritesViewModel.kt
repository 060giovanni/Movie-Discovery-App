package com.project.moviediscovery.ui.main.ui.favorite

import androidx.lifecycle.ViewModel
import com.project.moviediscovery.data.repo.FavoriteRepository

class FavoritesViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {
    fun getAllFavorite() = favoriteRepository.getAllFavorites()
}