package com.dermaseer.dermaseer.data.repository.predict

import com.dermaseer.dermaseer.data.remote.models.IngredientResponse
import okhttp3.RequestBody

interface IngredientRepository {
   suspend fun ingredientRecommendation(
      predictId: RequestBody,
      skinType: RequestBody,
      productCategory: RequestBody
   ): IngredientResponse
}