package com.dermaseer.dermaseer.data.repository.product

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dermaseer.dermaseer.data.remote.models.ProductResponse
import com.dermaseer.dermaseer.data.remote.services.ProductService
import com.dermaseer.dermaseer.data.local.datastore.AuthPreferences
import com.dermaseer.dermaseer.paging.ProductPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
   private val productService: ProductService,
   private val authPreferences: AuthPreferences
) : ProductRepository {

   override suspend fun getProductByCategory(category: String): Flow<PagingData<ProductResponse.Data>> {
      return Pager(
         config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
         ),
         pagingSourceFactory = {
            ProductPagingSource(productService, authPreferences, category)
         }
      ).flow
   }
}
