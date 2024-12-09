package com.dermaseer.dermaseer.data.remote.services

import com.dermaseer.dermaseer.data.remote.models.IngredientResponse
import com.dermaseer.dermaseer.data.remote.models.IngredientsRequest
import com.dermaseer.dermaseer.utils.ApiConstant
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface IngredientService {
   @POST(ApiConstant.INGREDIENT_RECOMMENDATION)
   suspend fun ingredientRecommendation(
      @Header("Authorization") token: String,
      @Body request: RequestBody
   ): IngredientResponse
}