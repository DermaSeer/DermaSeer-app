package com.dermaseer.dermaseer.ui.product_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaseer.dermaseer.data.remote.models.ProductRecommendationResponse
import com.dermaseer.dermaseer.data.repository.predict.ProductRecommendationRepository
import com.dermaseer.dermaseer.utils.ResultState
import com.dermaseer.dermaseer.utils.getDummyProductRecommendationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailRecomendationViewModel @Inject constructor(private val productRecommendationRepository: ProductRecommendationRepository) :
    ViewModel() {

    private val _productDetailRecommendation = MutableLiveData<ResultState>()
    val productDetailRecommendation: LiveData<ResultState> get() = _productDetailRecommendation

    private val _productRecommendation = MutableLiveData<ProductRecommendationResponse?>()
    val productRecommendation: LiveData<ProductRecommendationResponse?> get() = _productRecommendation

    fun fetchProductDetailRecommendation(resultId: String) {
        _productDetailRecommendation.value = ResultState.Loading
        viewModelScope.launch {
            try {
                val productRecommendation = productRecommendationRepository.getProductRecommendation(resultId)
                _productRecommendation.value = productRecommendation
                _productDetailRecommendation.value = ResultState.Success("Product recommendation loaded successfully")
            } catch (e: Exception) {
                _productDetailRecommendation.value = ResultState.Error("Failed to load product: ${e.message}")
            }
        }
    }

    fun loadDummyData(resultId: String) {
        _productRecommendation.value = getDummyProductRecommendationResponse()
        _productDetailRecommendation.value = ResultState.Success("Dummy product recommendation loaded successfully")
    }
}
