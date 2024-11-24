package com.dermaseer.dermaseer.data.repository.article

import com.dermaseer.dermaseer.data.remote.models.ArticleResponse

interface ArticleRepository {
   suspend fun getAllArticle(): ArticleResponse
}