package com.dermaseer.dermaseer.data.repository.predict

import com.dermaseer.dermaseer.data.remote.models.PredictResponse
import okhttp3.MultipartBody

interface PredictRepository {
   suspend fun predictModel(
      userPhoto: MultipartBody.Part
   ): PredictResponse
}