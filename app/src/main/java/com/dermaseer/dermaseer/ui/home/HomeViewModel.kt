package com.dermaseer.dermaseer.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.dermaseer.dermaseer.data.remote.models.ArticleResponse
import com.dermaseer.dermaseer.data.remote.models.UserResponse
import com.dermaseer.dermaseer.data.repository.article.ArticleRepository
import com.dermaseer.dermaseer.data.repository.user.UserRepository
import com.dermaseer.dermaseer.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
   private val articleRepository: ArticleRepository,
   private val userRepository: UserRepository
) : ViewModel() {

   private var _homeState = MutableLiveData<ResultState>()
   val homeState: LiveData<ResultState> = _homeState

   private var _articles = MutableLiveData<PagingData<ArticleResponse.Data>>()
   val articles: LiveData<PagingData<ArticleResponse.Data>> get() = _articles

   private var _userData = MutableLiveData<UserResponse>()
   val userData: LiveData<UserResponse> = _userData

   init {
      getArticles()
      getUser()
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
         } catch (e: HttpException) {
            Log.e("GetUser", "Error fetching user: ${e.message}")
            _homeState.value = ResultState.Error("Failed to load data: ${e.message}")
         }
      }
   }
}
