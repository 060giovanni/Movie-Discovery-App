package com.project.moviediscovery.data.remote.api

import com.project.moviediscovery.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .addHeader("Authorization", "Bearer ${BuildConfig.API_ACCESS}")
            .build()

        return chain.proceed(originalRequest)
    }
}