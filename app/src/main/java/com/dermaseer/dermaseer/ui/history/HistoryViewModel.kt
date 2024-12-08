package com.dermaseer.dermaseer.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaseer.dermaseer.data.repository.predict.HistoryRepository
import com.dermaseer.dermaseer.data.remote.models.HistoryResponse
import com.dermaseer.dermaseer.utils.ResultState
import com.dermaseer.dermaseer.utils.getDummyHistoryResponse
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
): ViewModel() {

    private val _historyData = MutableLiveData<HistoryResponse>()
    val historyData: LiveData<HistoryResponse> = _historyData

    private var _state = MutableLiveData<ResultState>()
    val state: LiveData<ResultState> = _state

    init {
//       getHistory()
        getDummyHistory()
    }

    private fun getHistory() {
        _state.value = ResultState.Loading
        viewModelScope.launch {
            try {
                _historyData.value = historyRepository.getHistory()
                _state.value = ResultState.Success("Success")
            } catch (e: Exception) {
                _state.value = ResultState.Error("Error")
            }
        }
    }

    private fun getDummyHistory() {
        _state.value = ResultState.Loading
        viewModelScope.launch {
            try {
                delay(1500)
                _historyData.value = getDummyHistoryResponse()
                _state.value = ResultState.Success("Success")
            } catch (e: Exception) {
                _state.value = ResultState.Error("Error")
            }
        }
    }
}
