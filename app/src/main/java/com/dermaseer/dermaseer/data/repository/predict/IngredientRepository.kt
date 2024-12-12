package com.dermaseer.dermaseer.data.repository.predict

import com.dermaseer.dermaseer.data.remote.models.IngredientResponse
import com.dermaseer.dermaseer.data.remote.models.IngredientsRequest
import okhttp3.RequestBody

interface IngredientRepository {
   suspend fun ingredientRecommendation(
      request: RequestBody
   ): IngredientResponse
}