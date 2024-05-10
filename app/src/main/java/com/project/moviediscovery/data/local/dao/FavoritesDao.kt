package com.project.moviediscovery.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.moviediscovery.data.models.FavoritesEntity

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addFavorite(favoriteEntity: FavoritesEntity)

    @Query("DELETE FROM favorites WHERE id =:id")
    fun removeFavorite(id: Int)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): LiveData<List<FavoritesEntity>>

    @Query("SELECT * FROM favorites WHERE id =:id")
    fun checkFavoriteMovie(id: Int): LiveData<List<FavoritesEntity>>
}