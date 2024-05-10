package com.project.moviediscovery.data.repo

import com.project.moviediscovery.data.remote.firebase.FirebaseHelper
import com.project.moviediscovery.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProfileRepository(
    private val firebaseHelper: FirebaseHelper
) {
    fun uploadProfilePic(email: String, image: ByteArray) = flow {
        emit(Result.Loading)
        try {
            val result = firebaseHelper.uploadProfilePic(email, image)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "failed."))
        }
    }.flowOn(Dispatchers.IO)

    fun getProfilePic(email: String) = flow {
        emit(Result.Loading)
        try {
            val imageResponse = firebaseHelper.getProfilePic(email)
            emit(Result.Success(imageResponse))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "failed."))
        }
    }.flowOn(Dispatchers.IO)

    fun deleteProfilePic(email: String) = flow {
        emit(Result.Loading)
        try {
            val imageResponse = firebaseHelper.deleteProfilePic(email)
            emit(Result.Success(imageResponse))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "failed."))
        }
    }.flowOn(Dispatchers.IO)

    fun getFirebaseCurrentUser() = firebaseHelper.getCurrentUser()
}