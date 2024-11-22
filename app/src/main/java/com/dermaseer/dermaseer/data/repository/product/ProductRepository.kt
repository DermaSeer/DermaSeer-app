package com.dermaseer.dermaseer.data.repository.product

import com.dermaseer.dermaseer.data.remote.models.ProductResponse

interface ProductRepository {
   suspend fun getProductByCategory(category: String): ProductResponse
}