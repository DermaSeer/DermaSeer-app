package com.dermaseer.dermaseer.data.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private val Context.datastore by preferencesDataStore("auth")
@Singleton
class AuthPreferences @Inject constructor(
   @ApplicationContext private val context: Context
) {
   companion object {
      private val TOKEN_KEY = stringPreferencesKey("auth_token")
   }

   suspend fun saveAuthToken(token: String?) {
      withContext(Dispatchers.IO) {
         Log.d("CheckToken", "Entered saveAuthToken with token: $token")
         context.datastore.edit { preferences ->
            Log.d("CheckToken", "Auth Preferences Save Token: $token")
            token?.let {
               Log.d("CheckToken", "Auth Preferences Save Token Store: $token")
               preferences[TOKEN_KEY] = token
            }
         }
      }
   }

   suspend fun clearSession() {
      withContext(Dispatchers.IO) {
         context.datastore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
         }
      }
   }

   val authToken: Flow<String?> = context.datastore.data
      .map { preferences ->
         preferences[TOKEN_KEY]
      }
}