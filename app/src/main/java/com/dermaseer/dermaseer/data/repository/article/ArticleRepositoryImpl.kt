package com.dermaseer.dermaseer.data.repository.article

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dermaseer.dermaseer.data.local.datastore.AuthPreferences
import com.dermaseer.dermaseer.data.remote.models.ArticleResponse
import com.dermaseer.dermaseer.data.remote.services.ArticleService
import com.dermaseer.dermaseer.paging.ArticlePagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
   private val authPreferences: AuthPreferences,
   private val apiService: ArticleService
) : ArticleRepository {

   override suspend fun getAllArticle(): Flow<PagingData<ArticleResponse.Data>> {
    return Pager(
       config = PagingConfig(
          pageSize = 10,
          enablePlaceholders = false
       ),
       pagingSourceFactory = {
          ArticlePagingSource(authPreferences, apiService)
       }
    ).flow
   }
}
