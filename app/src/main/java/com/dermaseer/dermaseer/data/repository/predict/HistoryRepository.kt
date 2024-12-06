package com.dermaseer.dermaseer.data.repository.predict

import com.dermaseer.dermaseer.data.remote.models.HistoryResponse

interface HistoryRepository {
   suspend fun getHistory(): HistoryResponse
}