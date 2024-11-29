package com.dermaseer.dermaseer.ui.product_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dermaseer.dermaseer.data.repository.product.ProductRepository
import com.dermaseer.dermaseer.data.remote.models.ProductResponse
import com.dermaseer.dermaseer.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
   private val productRepository: ProductRepository
) : ViewModel() {

   private val _productListState = MutableStateFlow<ResultState>(ResultState.Loading)
   val productListState: StateFlow<ResultState> = _productListState

   private var _productList: Flow<PagingData<ProductResponse.Data>>? = null
   val productList: Flow<PagingData<ProductResponse.Data>>?
      get() = _productList

   suspend fun getProductList(category: String) {
      if (_productList == null) {
         _productListState.value = ResultState.Loading
         try {
            _productList = productRepository.getProductByCategory(category)
               .cachedIn(viewModelScope)
            _productListState.value = ResultState.Success("Products successfully loaded")
         } catch (e: Exception) {
            _productListState.value = ResultState.Error("Failed to load products: ${e.message}")
         }
      }
   }
}


