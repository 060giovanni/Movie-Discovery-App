package com.project.moviediscovery.di

import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.project.moviediscovery.BuildConfig
import com.project.moviediscovery.data.local.MovieDiscoveryDatabase
import com.project.moviediscovery.data.remote.api.ApiService
import com.project.moviediscovery.data.remote.api.AuthInterceptor
import com.project.moviediscovery.data.remote.firebase.FirebaseHelper
import com.project.moviediscovery.data.repo.AuthRepository
import com.project.moviediscovery.data.repo.FavoriteRepository
import com.project.moviediscovery.data.repo.MovieRepository
import com.project.moviediscovery.data.repo.ProfileRepository
import com.project.moviediscovery.ui.auth.AuthViewModel
import com.project.moviediscovery.ui.detail.DetailViewModel
import com.project.moviediscovery.ui.main.ui.favorite.FavoritesViewModel
import com.project.moviediscovery.ui.main.ui.home.HomeViewModel
import com.project.moviediscovery.ui.main.ui.profile.ProfileViewModel
import com.project.moviediscovery.ui.search.SearchViewModel
import com.project.moviediscovery.utils.UserPreferences
import com.project.moviediscovery.utils.dataStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            MovieDiscoveryDatabase::class.java, "MovieDiscovery.db"
        ).fallbackToDestructiveMigration()
            .build()
    }
}

val networkModule = module {
    single { AuthInterceptor() }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_MOVIE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        if (BuildConfig.DEBUG) {
                            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                        } else {
                            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                        }
                    )
                    .addInterceptor(ChuckerInterceptor(androidContext()))
                    .addInterceptor(get<AuthInterceptor>())
                    .build()
            )
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val firebaseModule = module {
    single { FirebaseHelper() }
}

val dataStoreModule = module {
    single { androidContext().dataStore }
}

val preferencesModule = module {
    single { UserPreferences(get()) }
}

val repositoryModule = module {
    single { AuthRepository(get()) }
    single { MovieRepository(get()) }
    single { FavoriteRepository(get()) }
    single { ProfileRepository(get()) }
}

val viewModelModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { (movieId: Int) -> DetailViewModel(movieId, get(), get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
}
