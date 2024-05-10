package com.project.moviediscovery.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.moviediscovery.data.repo.AuthRepository
import com.project.moviediscovery.utils.UserPreferences
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    fun getIsLoggedIn() = userPreferences.getIsLoggedIn().asLiveData()

    fun loginUser(username: String, password: String) =
        authRepository.loginUser(username, password).asLiveData()

    fun registerUser(email: String, password: String) =
        authRepository.registerUser(email, password).asLiveData()

    fun savePreferences(
        accessToken: Boolean,
        email: String,
        profilePic: String,
    ) {
        viewModelScope.launch {
            userPreferences.savePreferences(
                accessToken,
                email, profilePic
            )
        }
    }
}