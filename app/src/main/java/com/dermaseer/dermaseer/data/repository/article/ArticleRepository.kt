package com.dermaseer.dermaseer.data.repository.article

import androidx.paging.PagingData
import com.dermaseer.dermaseer.data.remote.models.ArticleResponse
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
   suspend fun getAllArticle(): Flow<PagingData<ArticleResponse.Data>>
}