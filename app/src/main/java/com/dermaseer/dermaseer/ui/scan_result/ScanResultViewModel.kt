package com.dermaseer.dermaseer.ui.scan_result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaseer.dermaseer.data.remote.models.PredictResponse
import com.dermaseer.dermaseer.data.repository.predict.PredictRepository
import com.dermaseer.dermaseer.utils.ResultState
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

    private var _state = MutableLiveData<ResultState>()
    val state: LiveData<ResultState> = _state

    fun fetchPrediction(image: MultipartBody.Part) {
        viewModelScope.launch {
            _state.value = ResultState.Loading
            try {
                val response = predictRepository.predictModel(image)
                _predictResponse.value = response
                _state.value = ResultState.Success("Success")
            } catch (e: Exception) {
                _state.value = ResultState.Error("${e.message}")
            }
        }
    }
}
