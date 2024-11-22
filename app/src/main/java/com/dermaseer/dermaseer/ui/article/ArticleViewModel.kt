package com.dermaseer.dermaseer.ui.article

import androidx.lifecycle.ViewModel
import com.dermaseer.dermaseer.data.repository.article.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
   private val articleRepository: ArticleRepository
): ViewModel() {

}