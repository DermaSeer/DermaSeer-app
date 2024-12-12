package com.dermaseer.dermaseer.data.repository.predict

import com.dermaseer.dermaseer.data.remote.models.ProductRecommendationResponse

interface ProductRecommendationRepository {
   suspend fun getProductRecommendation(
      resultId: String
   ): ProductRecommendationResponse
}