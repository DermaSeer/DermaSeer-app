package com.dermaseer.dermaseer.data.repository.user

import android.util.Log
import com.dermaseer.dermaseer.data.local.datastore.AuthPreferences
import com.dermaseer.dermaseer.data.remote.models.DeleteUserResponse
import com.dermaseer.dermaseer.data.remote.models.UserResponse
import com.dermaseer.dermaseer.data.remote.services.UserService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import okhttp3.RequestBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
   private val userService: UserService,
   private val authPreferences: AuthPreferences
) : UserRepository {

   companion object {
      private const val TAG = "UserRepositoryImpl"
   }

   override suspend fun getCurrentUser(): UserResponse {
      return safeApiCall {
         val token = authPreferences.authToken.first()
         Log.d(TAG, "Token in repo: $token")
         if (token != null) {
            userService.getCurrentUser("Bearer $token")
         } else {
            throw IllegalStateException("Token is not available")
         }
      }
   }

   override suspend fun updateUser(
      name: RequestBody,
      age: RequestBody,
      gender: RequestBody,
      profilePicture: RequestBody
   ): UserResponse {
      return safeApiCall {
         val token = authPreferences.authToken.first()
         if (token != null) {
            userService.updateUser(
               "Bearer $token",
               name,
               age,
               gender,
               profilePicture
            )
         } else {
            throw IllegalStateException("Token is not available")
         }
      }
   }

   override suspend fun deleteUser(): DeleteUserResponse {
      return safeApiCall {
         val token = authPreferences.authToken.first()
         if (token != null) {
            userService.deleteUser("Bearer $token")
         } else {
            throw IllegalStateException("Token is not available")
         }
      }
   }

   private suspend fun <T> safeApiCall(call: suspend () -> T): T {
      return try {
         call()
      } catch (e: HttpException) {
         if (e.code() == 401) {
            Log.w(TAG, "Token expired, refreshing token...")
            val newToken = refreshFirebaseToken()
            if (newToken != null) {
               // Retry with new token
               Log.d(TAG, "Retrying request with refreshed token")
               call()
            } else {
               throw IllegalStateException("Unable to refresh token")
            }
         } else {
            throw e
         }
      }
   }

   private suspend fun refreshFirebaseToken(): String? {
      return try {
         val user = FirebaseAuth.getInstance().currentUser
         val result = user?.getIdToken(true)?.await()
         val newToken = result?.token
         if (newToken != null) {
            authPreferences.saveAuthToken(newToken)
            Log.d(TAG, "Token refreshed successfully")
         } else {
            Log.e(TAG, "Failed to refresh token: Token is null")
         }
         newToken
      } catch (e: Exception) {
         Log.e(TAG, "Error refreshing token")
         null
      }
   }
}
