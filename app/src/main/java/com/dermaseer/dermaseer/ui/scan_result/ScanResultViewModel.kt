package com.dermaseer.dermaseer.ui.scan_result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaseer.dermaseer.data.remote.models.PredictResponse
import com.dermaseer.dermaseer.data.repository.predict.PredictRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ScanResultViewModel @Inject constructor(
    private val predictRepository: PredictRepository
) : ViewModel() {

    private val _predictResponse = MutableLiveData<PredictResponse?>()
    val predictResponse: LiveData<PredictResponse?> get() = _predictResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun fetchPrediction(image: MultipartBody.Part) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = predictRepository.predictModel(image)
                _predictResponse.value = response
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _predictResponse.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}
