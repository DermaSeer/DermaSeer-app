package com.dermaseer.dermaseer.data.repository.product

import com.dermaseer.dermaseer.data.local.datastore.AuthPreferences
import com.dermaseer.dermaseer.data.remote.models.ProductResponse
import com.dermaseer.dermaseer.data.remote.services.ProductService
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
   private val productService: ProductService,
   private val authPreferences: AuthPreferences
): ProductRepository {
   override suspend fun getProductByCategory(category: String): ProductResponse {
      val token = authPreferences.authToken.first()
      return if (token != null) {
         productService.getProductByCategory("Bearer $token", category)
      } else {
         throw IllegalStateException("Token is not available")
      }
   }
}