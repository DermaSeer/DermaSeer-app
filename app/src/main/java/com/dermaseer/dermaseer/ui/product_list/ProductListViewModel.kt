package com.dermaseer.dermaseer.ui.product_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaseer.dermaseer.data.repository.product.ProductRepository
import com.dermaseer.dermaseer.utils.ResultState
import com.dermaseer.dermaseer.data.remote.models.ProductResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
   private val productRepository: ProductRepository
) : ViewModel() {

   private val _productListState = MutableStateFlow<ResultState>(ResultState.Loading)
   val productListState: StateFlow<ResultState> = _productListState

   private val _productList = MutableStateFlow<List<ProductResponse.Data>>(emptyList())
   val productList: StateFlow<List<ProductResponse.Data>> = _productList

   fun getProductList(category: String) {
      viewModelScope.launch {
         _productListState.value = ResultState.Loading
         try {
            val response = productRepository.getProductByCategory(category)
            val filteredList = response.data
               ?.filterNotNull()
               ?: emptyList()

            if (filteredList.isEmpty()) {
               _productListState.value = ResultState.Error("No products found in this category.")
            } else {
               _productList.value = filteredList
               _productListState.value = ResultState.Success("Products successfully loaded")
            }
         } catch (e: Exception) {
            _productListState.value = ResultState.Error("Failed to load products: ${e.message}")
         }
      }
   }
}
