package com.dermaseer.dermaseer.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dermaseer.dermaseer.data.remote.models.ArticleResponse
import com.dermaseer.dermaseer.data.repository.article.ArticleRepository
import com.dermaseer.dermaseer.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
   private val articleRepository: ArticleRepository
) : ViewModel() {

   private val _articleState = MutableStateFlow<ResultState>(ResultState.Loading)
   val articleState: StateFlow<ResultState> = _articleState

   private var _articleList: Flow<PagingData<ArticleResponse.Data>>? = null
   val articleList: Flow<PagingData<ArticleResponse.Data>>?
      get() = _articleList

   suspend fun getArticles() {
      if (_articleList == null) {
         _articleState.value = ResultState.Loading
         try {
            _articleList = articleRepository.getAllArticle()
               .cachedIn(viewModelScope)
            _articleState.value = ResultState.Success("Articles loaded successfully")
         } catch (e: Exception) {
            _articleState.value = ResultState.Error("Failed to load articles: ${e.message}")
         }
      }
   }
}
