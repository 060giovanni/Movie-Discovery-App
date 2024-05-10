package com.project.moviediscovery.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.project.moviediscovery.utils.UserPreferences.Companion.USER_PREFERENCES_NAME
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES_NAME)

class UserPreferences(private val dataStore: DataStore<Preferences>) {
    fun getIsLoggedIn() = dataStore.data.map { it[PREF_LOGGED_IN] ?: false }
    fun getEmail() = dataStore.data.map { it[PREF_EMAIL] ?: preferenceDefaultValue }
    fun getPhone() = dataStore.data.map { it[PREF_PHONE] ?: preferenceDefaultValue }
    fun getProfilePic() = dataStore.data.map { it[PREF_PROFILE_PIC] ?: preferenceDefaultValue }

    suspend fun savePreferences(
        isLoggedIn: Boolean,
        email: String,
        profilePic: String
    ) {
        dataStore.edit { prefs ->
            prefs[PREF_LOGGED_IN] = isLoggedIn
            prefs[PREF_EMAIL] = email
            prefs[PREF_PROFILE_PIC] = profilePic
        }
    }

    suspend fun saveIsLoggedIn(
        isLoggedIn: Boolean,
    ) {
        dataStore.edit { prefs ->
            prefs[PREF_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun clearPreferences() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    companion object {
        const val USER_PREFERENCES_NAME = "userPreferences"

        val PREF_LOGGED_IN = booleanPreferencesKey("pref_logged_in")
        val PREF_EMAIL = stringPreferencesKey("pref_email")
        val PREF_PHONE = stringPreferencesKey("pref_phone")
        val PREF_PROFILE_PIC = stringPreferencesKey("pref_profile_pic")

        const val preferenceDefaultValue: String = "preferences_default_value"
    }
}