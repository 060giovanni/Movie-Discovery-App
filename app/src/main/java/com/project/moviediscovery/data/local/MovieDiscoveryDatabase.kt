package com.project.moviediscovery.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.moviediscovery.data.local.dao.FavoritesDao
import com.project.moviediscovery.data.models.FavoritesEntity

@Database(entities = [FavoritesEntity::class], version = 1, exportSchema = true)
abstract class MovieDiscoveryDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}