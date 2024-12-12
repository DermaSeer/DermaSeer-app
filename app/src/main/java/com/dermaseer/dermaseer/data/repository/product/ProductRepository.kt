package com.dermaseer.dermaseer.data.repository.product

import androidx.paging.PagingData
import com.dermaseer.dermaseer.data.remote.models.ProductResponse
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
   suspend fun getProductByCategory(category: String): Flow<PagingData<ProductResponse.Data>>
}