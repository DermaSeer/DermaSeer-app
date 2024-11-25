package com.dermaseer.dermaseer.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaseer.dermaseer.data.repository.article.ArticleRepository
import com.dermaseer.dermaseer.data.remote.models.ArticleResponse
import com.dermaseer.dermaseer.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
   private val articleRepository: ArticleRepository
) : ViewModel() {

   private val _articleState = MutableLiveData<ResultState>()
   val articleState: LiveData<ResultState> get() = _articleState

   private val _articles = MutableLiveData<List<ArticleResponse.Data?>>()
   val articles: LiveData<List<ArticleResponse.Data?>> get() = _articles

   fun getArticles() {
      _articleState.value = ResultState.Loading
      viewModelScope.launch {
         try {
            val response = withContext(Dispatchers.IO) {
               articleRepository.getAllArticle()
            }
            _articles.value = response.data?.filterNotNull()
            _articleState.value = ResultState.Success("Articles loaded successfully")
         } catch (e: Exception) {
            _articleState.value = ResultState.Error("Failed to load articles: ${e.message}")
         }
      }
   }
}

