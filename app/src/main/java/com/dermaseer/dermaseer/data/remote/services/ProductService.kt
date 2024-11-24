package com.dermaseer.dermaseer.data.remote.services

import com.dermaseer.dermaseer.data.remote.models.ProductResponse
import com.dermaseer.dermaseer.utils.ApiConstant
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ProductService {
   @GET(ApiConstant.GET_PRODUCT_BY_CATEGORY)
   suspend fun getProductByCategory(
      @Header("Authorization") token: String,
      @Query("category") category: String
   ): ProductResponse
}