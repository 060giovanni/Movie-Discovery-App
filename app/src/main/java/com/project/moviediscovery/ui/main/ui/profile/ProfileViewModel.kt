package com.project.moviediscovery.ui.main.ui.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.moviediscovery.data.repo.ProfileRepository
import com.project.moviediscovery.utils.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.project.moviediscovery.utils.Result
import kotlinx.coroutines.runBlocking

class ProfileViewModel(
    private val userPreferences: UserPreferences,
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    fun getEmail() = userPreferences.getEmail().asLiveData()
    val email = runBlocking { userPreferences.getEmail().first() }
    val profilePic = MutableLiveData<Uri?>()

    val canDeleteProfilePic = MutableLiveData(false)

    init {
        getProfilePic()
    }

    fun clearPreferences() {
        viewModelScope.launch {
            userPreferences.clearPreferences()
        }
    }

    fun uploadProfilePic(image: ByteArray) =
        profileRepository.uploadProfilePic(email, image).asLiveData()

    fun deleteProfilePic() {
        viewModelScope.launch {
            profileRepository.deleteProfilePic(email).asLiveData().asFlow().collect {
                profilePic.postValue(null)
                canDeleteProfilePic.postValue(false)
            }
        }
    }

    private fun getProfilePic() {
        viewModelScope.launch {
            profileRepository.getProfilePic(email).asLiveData().asFlow().collect { result ->
                if (result is Result.Success) {
                    profilePic.postValue(result.data)
                    canDeleteProfilePic.postValue(true)
                } else {
                    profilePic.postValue(null)
                    canDeleteProfilePic.postValue(false)
                }
            }
        }
    }

    fun getFirebaseCurrentUser() = profileRepository.getFirebaseCurrentUser()
}