package com.dermaseer.dermaseer.data.remote.services

import com.dermaseer.dermaseer.data.remote.models.ArticleResponse
import com.dermaseer.dermaseer.utils.ApiConstant
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ArticleService {
   @GET(ApiConstant.GET_ALL_ARTICLE)
   suspend fun getAllArticle(
      @Header("Authorization") token: String,
      @Query("page") page: Int,
      @Query("size") size: Int = 10
   ): ArticleResponse
}