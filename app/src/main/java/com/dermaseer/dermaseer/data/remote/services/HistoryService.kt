package com.dermaseer.dermaseer.data.remote.services

import com.dermaseer.dermaseer.data.remote.models.HistoryResponse
import com.dermaseer.dermaseer.utils.ApiConstant
import retrofit2.http.GET
import retrofit2.http.Header

interface HistoryService {
   @GET(ApiConstant.GET_HISTORY)
   suspend fun getHistory(
      @Header("Authorization") token: String
   ): HistoryResponse
}