package com.project.moviediscovery.data.repo

import com.project.moviediscovery.data.local.MovieDiscoveryDatabase
import com.project.moviediscovery.data.local.dao.FavoritesDao
import com.project.moviediscovery.data.models.FavoritesEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(movieDiscoveryDatabase: MovieDiscoveryDatabase) {
    private val dao: FavoritesDao = movieDiscoveryDatabase.favoritesDao()
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    fun addFavorite(favorite: FavoritesEntity) {
        executorService.execute { dao.addFavorite(favorite) }
    }

    fun removeFavorite(id: Int) {
        executorService.execute { dao.removeFavorite(id) }
    }

    fun getAllFavorites() = dao.getAllFavorites()

    fun checkFavoriteMovie(id: Int) = dao.checkFavoriteMovie(id)
}