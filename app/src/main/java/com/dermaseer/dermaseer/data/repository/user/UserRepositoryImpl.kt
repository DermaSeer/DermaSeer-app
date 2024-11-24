package com.dermaseer.dermaseer.data.repository.user

import android.util.Log
import com.dermaseer.dermaseer.data.local.datastore.AuthPreferences
import com.dermaseer.dermaseer.data.remote.models.DeleteUserResponse
import com.dermaseer.dermaseer.data.remote.models.UserResponse
import com.dermaseer.dermaseer.data.remote.services.UserService
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
   private val userService: UserService,
   private val authPreferences: AuthPreferences
): UserRepository {
   override suspend fun getCurrentUser(): UserResponse {
      val token = authPreferences.authToken.first()
      Log.d("CheckTokenRepo", "Token in repo: $token")
      return if (token != null) {
         userService.getCurrentUser("Bearer $token")
      } else {
         throw IllegalStateException("Token is not available")
      }
   }

   override suspend fun updateUser(
      name: RequestBody,
      birthday: RequestBody,
      gender: RequestBody,
      profilePicture: RequestBody
   ): UserResponse {
      val token = authPreferences.authToken.first()
      return if (token != null) {
         userService.updateUser(
            "Bearer $token",
            name,
            birthday,
            gender,
            profilePicture
         )
      } else {
         throw IllegalStateException("Token is not available")
      }
   }

   override suspend fun deleteUser(): DeleteUserResponse {
      val token = authPreferences.authToken.first()
      return if (token != null) {
         userService.deleteUser("Bearer $token")
      } else {
         throw IllegalStateException("Token is not available")
      }
   }
}