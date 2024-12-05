package com.dermaseer.dermaseer.data.remote.services

import com.dermaseer.dermaseer.data.remote.models.DeleteUserResponse
import com.dermaseer.dermaseer.data.remote.models.UserResponse
import com.dermaseer.dermaseer.utils.ApiConstant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserService {
   @GET(ApiConstant.GET_CURRENT_USER)
   suspend fun getCurrentUser(
      @Header("Authorization") token: String
   ): UserResponse

   @Multipart
   @POST(ApiConstant.UPDATE_USER)
   suspend fun updateUser(
      @Header("Authorization") token: String,
      @Part("name") name: RequestBody,
      @Part("age") age: RequestBody,
      @Part("gender") gender: RequestBody,
      @Part("profile_picture") profilePicture: RequestBody
   ): UserResponse

   @DELETE(ApiConstant.DELETE_CURRENT_USER)
   suspend fun deleteUser(
      @Header("Authorization") token: String
   ): DeleteUserResponse
}