package com.dermaseer.dermaseer.data.repository.predict

import com.dermaseer.dermaseer.data.remote.models.IngredientResponse

interface IngredientRepository {
   suspend fun ingredientRecommendation(
      predictId: String,
      skinType: String,
      productCategory: String
   ): IngredientResponse
}