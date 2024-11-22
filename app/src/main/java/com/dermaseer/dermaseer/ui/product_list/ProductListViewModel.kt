package com.dermaseer.dermaseer.ui.product_list

import androidx.lifecycle.ViewModel
import com.dermaseer.dermaseer.data.repository.product.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
   private val productRepository: ProductRepository
): ViewModel() {

}