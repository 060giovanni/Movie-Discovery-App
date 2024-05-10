package com.project.moviediscovery.data.repo

import com.project.moviediscovery.data.remote.firebase.FirebaseHelper
import com.project.moviediscovery.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepository(
    private val firebaseHelper: FirebaseHelper
) {
    fun loginUser(email: String, password: String) = flow {
        emit(Result.Loading)
        try {
            val authResult = firebaseHelper.loginUser(email, password)
            val user = authResult.user
            emit(Result.Success(user))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Authentication failed."))
        }
    }.flowOn(Dispatchers.IO)

    fun registerUser(email: String, password: String) = flow {
        emit(Result.Loading)
        try {
            val authResult = firebaseHelper.registerUser(email, password)
            val user = authResult.user
            emit(Result.Success(user))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Authentication failed."))
        }
    }.flowOn(Dispatchers.IO)
}