package com.dermaseer.dermaseer.data.repository.article

import com.dermaseer.dermaseer.data.local.datastore.AuthPreferences
import com.dermaseer.dermaseer.data.remote.models.ArticleResponse
import com.dermaseer.dermaseer.data.remote.services.ArticleService
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
   private val apiService: ArticleService,
   private val authPreferences: AuthPreferences
) : ArticleRepository {

   override suspend fun getAllArticle(): ArticleResponse {
      val token = authPreferences.authToken.first()

      return if (token.isNullOrEmpty()) {
         throw IllegalStateException("Token is null or empty")
      } else {
         apiService.getAllArticle("Bearer $token")
      }
   }
}
