package com.dermaseer.dermaseer.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dermaseer.dermaseer.data.remote.models.ProductResponse
import com.dermaseer.dermaseer.data.remote.services.ProductService
import com.dermaseer.dermaseer.data.local.datastore.AuthPreferences
import kotlinx.coroutines.flow.first

class ProductPagingSource(
    private val productService: ProductService,
    private val authPreferences: AuthPreferences,
    private val category: String
) : PagingSource<Int, ProductResponse.Data>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductResponse.Data> {
        val page = params.key ?: INITIAL_PAGE_INDEX
        return try {
            val token = authPreferences.authToken.first()
            if (token != null) {
                val response = productService.getProductByCategory("Bearer $token", category, page)
                val products = response.data?.filterNotNull() ?: emptyList()

                LoadResult.Page(
                    data = products,
                    prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                    nextKey = if (products.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(IllegalStateException("Token is not available"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductResponse.Data>): Int? {
        return state.anchorPosition?.let { position ->
            val closestPage = state.closestPageToPosition(position)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }
}
