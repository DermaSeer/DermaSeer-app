package com.dermaseer.dermaseer.data.repository.user

import com.dermaseer.dermaseer.data.remote.models.DeleteUserResponse
import com.dermaseer.dermaseer.data.remote.models.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface UserRepository {
   suspend fun getCurrentUser(): UserResponse

   suspend fun updateUser(
      name: RequestBody,
      age: RequestBody,
      gender: RequestBody,
      profilePicture: RequestBody
   ): UserResponse

   suspend fun deleteUser(): DeleteUserResponse
}