package com.dermaseer.dermaseer.data.remote.services

import com.dermaseer.dermaseer.data.remote.models.ProductRecommendationResponse
import com.dermaseer.dermaseer.utils.ApiConstant
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ProductRecommendationService {
   @GET(ApiConstant.GET_PRODUCT_RECOMMENDATION)
   suspend fun getProductRecommendation(
      @Header("Authorization") token: String,
      @Query("result_id") resultId: String
   ): ProductRecommendationResponse
}