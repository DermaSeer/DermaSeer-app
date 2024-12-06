package com.dermaseer.dermaseer.data.remote.services

import com.dermaseer.dermaseer.data.remote.models.IngredientResponse
import com.dermaseer.dermaseer.utils.ApiConstant
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface IngredientService {
   @POST(ApiConstant.INGREDIENT_RECOMMENDATION)
   suspend fun ingredientRecommendation(
      @Header("Authorization") token: String,
      @Query("predict_id") predictId: String,
      @Query("skin_type") skinType: String,
      @Query("product_category") productCategory: String
   ): IngredientResponse
}