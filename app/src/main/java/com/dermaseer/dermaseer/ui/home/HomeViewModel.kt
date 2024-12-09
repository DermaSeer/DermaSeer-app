package com.dermaseer.dermaseer.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.dermaseer.dermaseer.data.remote.models.ArticleResponse
import com.dermaseer.dermaseer.data.remote.models.HistoryResponse
import com.dermaseer.dermaseer.data.remote.models.UserResponse
import com.dermaseer.dermaseer.data.repository.article.ArticleRepository
import com.dermaseer.dermaseer.data.repository.predict.HistoryRepository
import com.dermaseer.dermaseer.data.repository.user.UserRepository
import com.dermaseer.dermaseer.utils.ResultState
import com.dermaseer.dermaseer.utils.getDummyHistoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
   private val articleRepository: ArticleRepository,
   private val userRepository: UserRepository,
   private val historyRepository: HistoryRepository
) : ViewModel() {

   private var _homeState = MutableLiveData<ResultState>()
   val homeState: LiveData<ResultState> = _homeState

   private var _articles = MutableLiveData<PagingData<ArticleResponse.Data>>()
   val articles: LiveData<PagingData<ArticleResponse.Data>> get() = _articles

   private var _userData = MutableLiveData<UserResponse>()
   val userData: LiveData<UserResponse> = _userData

   private var _latestHistory = MutableLiveData<HistoryResponse>()
   val latestHistory: LiveData<HistoryResponse> = _latestHistory

   init {
      getArticles()
      getUser()
      getDummyLatestHistory()
   }

   private fun getArticles() {
      _homeState.value = ResultState.Loading
      viewModelScope.launch {
         try {
            articleRepository.getAllArticle().collectLatest { pagingData ->
                  _articles.value = pagingData // Directly assign PagingData
            }
            _homeState.value = ResultState.Success("Data loaded successfully")
         } catch (e: Exception) {
            _homeState.value = ResultState.Error("Failed to load data: ${e.message}")
         }
      }
   }

   private fun getUser() {
      _homeState.value = ResultState.Loading
      viewModelScope.launch {
         try {
            _userData.value = userRepository.getCurrentUser()
            _homeState.value = ResultState.Success("Data loaded successfully")
         } catch (e: Exception) {
            Log.e("GetUser", "Error fetching user: ${e.message}")
            _homeState.value = ResultState.Error("Failed to load data: ${e.message}")
         }
      }
   }

   private fun getLatestHistory() {
      _homeState.value = ResultState.Loading
      viewModelScope.launch {
         try {
            _latestHistory.value = historyRepository.getHistory()
            _homeState.value = ResultState.Success("Success")
         } catch (e: Exception) {
            _homeState.value = ResultState.Error("Please, close the app: ${e.message}")
         }
      }
   }

   private fun getDummyLatestHistory() {
      _homeState.value = ResultState.Loading
      viewModelScope.launch {
         try {
            delay(1500)
            _latestHistory.value = getDummyHistoryResponse()
            _homeState.value = ResultState.Success("Success")
         } catch (e: Exception) {
            _homeState.value = ResultState.Error("Error")
         }
      }
   }
}
