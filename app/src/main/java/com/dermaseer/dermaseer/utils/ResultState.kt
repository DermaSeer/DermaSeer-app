package com.dermaseer.dermaseer.utils

sealed class ResultState(val message: String) {
   data object Loading : ResultState("Loading...")
   data class Success(val successMessage: String) : ResultState(successMessage)
   data class Error(val errorMessage: String) : ResultState(errorMessage)
}