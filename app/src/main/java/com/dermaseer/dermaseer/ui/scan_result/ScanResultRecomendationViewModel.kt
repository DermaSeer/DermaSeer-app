package com.dermaseer.dermaseer.ui.scan_result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaseer.dermaseer.data.remote.models.IngredientResponse
import com.dermaseer.dermaseer.data.remote.models.IngredientsRequest
import com.dermaseer.dermaseer.data.remote.models.ProductRecommendationResponse
import com.dermaseer.dermaseer.data.repository.predict.IngredientRepository
import com.dermaseer.dermaseer.data.repository.predict.ProductRecommendationRepository
import com.dermaseer.dermaseer.utils.ResultState
import com.dermaseer.dermaseer.utils.getDummyIngredientResponse
import com.dermaseer.dermaseer.utils.getDummyProductRecommendationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ScanResultRecomendationViewModel @Inject constructor(
    private val productRecommendationRepository: ProductRecommendationRepository,
    private val ingredientRepository: IngredientRepository
) : ViewModel() {

    private val _ingredientsResponse = MutableLiveData<IngredientResponse?>()
    val ingredientsResponse: MutableLiveData<IngredientResponse?> get() = _ingredientsResponse

    private val _productRecommendationResponse = MutableLiveData<ProductRecommendationResponse?>()
    val productRecommendationResponse: LiveData<ProductRecommendationResponse?> get() = _productRecommendationResponse

    private var _state = MutableLiveData<ResultState>()
    val state: LiveData<ResultState> = _state

    fun fetchAllRecommendations(request: RequestBody) {
        viewModelScope.launch {
            _state.value = ResultState.Loading
            try {
                val ingredientResponse = ingredientRepository.ingredientRecommendation(
                    request
                )
                _ingredientsResponse.value = ingredientResponse
                val resultId = ingredientResponse.data?.resultId ?: throw Exception("Result ID not found")
                val productResponse = productRecommendationRepository.getProductRecommendation(resultId)
                _productRecommendationResponse.value = productResponse
                _state.value = ResultState.Success("Recommendations fetched successfully")
            } catch (e: Exception) {
                _state.value = ResultState.Error("${e.message}")
            }
        }
    }

    fun fetchDummyAllRecommendations() {
        viewModelScope.launch {
            _state.value = ResultState.Loading
            try {
                delay(1500)
                _ingredientsResponse.value = getDummyIngredientResponse()
                _productRecommendationResponse.value = getDummyProductRecommendationResponse()
                _state.value = ResultState.Success("Recommendations fetched successfully")
            } catch (e: Exception) {
                _state.value = ResultState.Error("${e.message}")
            }
        }
    }
}
