package com.dermaseer.dermaseer.ui.history_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaseer.dermaseer.data.remote.models.HistoryResponse
import com.dermaseer.dermaseer.data.repository.predict.HistoryRepository
import com.dermaseer.dermaseer.utils.ResultState
import com.dermaseer.dermaseer.utils.getDummyHistoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryDetailViewModel @Inject constructor(
   private val historyRepository: HistoryRepository
): ViewModel() {
   private var _state = MutableLiveData<ResultState>()
   val state: LiveData<ResultState> = _state

   private var _historyData = MutableLiveData<HistoryResponse.Data>()
   val historyData: LiveData<HistoryResponse.Data> = _historyData

   fun getHistoryByIndex(index: Int) {
      _state.value = ResultState.Loading
      viewModelScope.launch {
         try {
            _historyData.value = historyRepository.getHistory().data?.get(index)
            _state.value = ResultState.Success("Success")
         } catch (e: Exception) {
            _state.value = ResultState.Error("Error")
         }
      }
   }

   fun getDummyHistoryByIndex(index: Int) {
      _state.value = ResultState.Loading
      viewModelScope.launch {
         try {
            _historyData.value = getDummyHistoryResponse().data?.get(index)
            _state.value = ResultState.Success("Success")
         } catch (e: Exception) {
            _state.value = ResultState.Error("Error")
         }
      }
   }
}