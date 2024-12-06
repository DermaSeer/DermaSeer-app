package com.dermaseer.dermaseer.ui.scan_result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaseer.dermaseer.data.remote.models.ProductRecommendationResponse
import com.dermaseer.dermaseer.data.repository.predict.ProductRecommendationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanResultRecomendationViewModel @Inject constructor(
    private val productRecommendationRepository: ProductRecommendationRepository
) : ViewModel() {

    private val _productRecommendationResponse = MutableLiveData<ProductRecommendationResponse?>()
    val productRecommendationResponse: LiveData<ProductRecommendationResponse?> get() = _productRecommendationResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun fetchProductRecommendation(resultId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = productRecommendationRepository.getProductRecommendation(resultId)
                _productRecommendationResponse.value = response
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _productRecommendationResponse.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}
