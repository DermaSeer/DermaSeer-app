package com.dermaseer.dermaseer.ui.product_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dermaseer.dermaseer.data.repository.predict.ProductRecommendationRepository
import com.dermaseer.dermaseer.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailRecomendationViewModel @Inject constructor(private val productRecommendationRepository: ProductRecommendationRepository) :
    ViewModel() {
    private val _productDetailRecommendation = MutableLiveData<ResultState>()
    val productDetailRecommendation: LiveData<ResultState> get() = _productDetailRecommendation

    suspend fun fetchProductDetailRecommendation(resultId: String) {
        _productDetailRecommendation.value = ResultState.Loading
        try {
            val product = productRecommendationRepository.getProductRecommendation(resultId)
            _productDetailRecommendation.value = ResultState.Success(product.toString())
        } catch (e: Exception) {
            _productDetailRecommendation.value = ResultState.Error("Failed to load product: ${e.message}")
        }
    }

}