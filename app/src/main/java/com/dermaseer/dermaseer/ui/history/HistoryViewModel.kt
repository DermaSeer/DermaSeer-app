package com.dermaseer.dermaseer.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaseer.dermaseer.data.repository.predict.HistoryRepository
import com.dermaseer.dermaseer.data.remote.models.HistoryResponse
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val historyRepository: HistoryRepository) :
    ViewModel() {

    private val _historyData = MutableLiveData<List<HistoryResponse.Data>?>()
    val historyData: LiveData<List<HistoryResponse.Data>?> = _historyData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getHistory() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = historyRepository.getHistory()
                if (response.success == true) {
                    _historyData.value = response.data?.filterNotNull()
                } else {
                    _errorMessage.value = response.message ?: "Unknown error"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
