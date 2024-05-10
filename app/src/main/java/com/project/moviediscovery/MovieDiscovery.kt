package com.project.moviediscovery

import android.app.Application
import com.project.moviediscovery.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MovieDiscovery: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MovieDiscovery)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    firebaseModule,
                    dataStoreModule,
                    preferencesModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }
}