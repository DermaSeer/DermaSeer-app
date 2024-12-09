package com.dermaseer.dermaseer.data.remote.services

import com.dermaseer.dermaseer.data.remote.models.PredictResponse
import com.dermaseer.dermaseer.utils.ApiConstant
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PredictService {
   @Multipart
   @POST(ApiConstant.PREDICT_MODEL)
   suspend fun predictModel(
      @Header("Authorization") token: String,
      @Part userPhoto: MultipartBody.Part
   ): PredictResponse
}