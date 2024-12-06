package com.dermaseer.dermaseer.ui.scan_result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaseer.dermaseer.data.remote.models.IngredientResponse
import com.dermaseer.dermaseer.data.repository.predict.IngredientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientViewModel @Inject constructor(private val ingredientRepository: IngredientRepository) :
    ViewModel() {
    private val _ingredientsResponse = MutableLiveData<IngredientResponse?>()
    val ingredientsResponse: MutableLiveData<IngredientResponse?> get() = _ingredientsResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> get() = _errorMessage

    fun fetchIngredients(predictId: String, skinType: String, productCategory: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ingredientRepository.ingredientRecommendation(
                    predictId,
                    skinType,
                    productCategory
                )
                _ingredientsResponse.value = response

                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _ingredientsResponse.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}