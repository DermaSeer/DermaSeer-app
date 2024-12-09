package com.dermaseer.dermaseer.data.repository.predict

import android.util.Log
import com.dermaseer.dermaseer.data.local.datastore.AuthPreferences
import com.dermaseer.dermaseer.data.remote.models.IngredientResponse
import com.dermaseer.dermaseer.data.remote.services.IngredientService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import okhttp3.RequestBody
import retrofit2.HttpException
import javax.inject.Inject

class IngredientRepositoryImpl @Inject constructor(
   private val ingredientService: IngredientService,
   private val authPreferences: AuthPreferences
) : IngredientRepository {
   override suspend fun ingredientRecommendation(
      predictId: RequestBody,
      skinType: RequestBody,
      productCategory: RequestBody
   ): IngredientResponse {
      return safeApiCall {
         val token = authPreferences.authToken.first()
         Log.d(TAG, "Token in repo: $token")
         if (token != null) {
            ingredientService.ingredientRecommendation("Bearer $token", predictId, skinType, productCategory)
         } else {
            throw IllegalStateException("Token is not available")
         }
      }
   }

   private suspend fun <T> safeApiCall(call: suspend () -> T): T {
      return try {
         call()
      } catch (e: HttpException) {
         if (e.code() == 401) {
            Log.w(TAG, "Token expired, refreshing token...")
            val newToken = refreshFirebaseToken()
            if (newToken != null) {
               // Retry with new token
               Log.d(TAG, "Retrying request with refreshed token")
               call()
            } else {
               throw IllegalStateException("Unable to refresh token")
            }
         } else {
            throw e
         }
      }
   }

   private suspend fun refreshFirebaseToken(): String? {
      return try {
         val user = FirebaseAuth.getInstance().currentUser
         val result = user?.getIdToken(true)?.await()
         val newToken = result?.token
         if (newToken != null) {
            authPreferences.saveAuthToken(newToken)
            Log.d(TAG, "Token refreshed successfully")
         } else {
            Log.e(TAG, "Failed to refresh token: Token is null")
         }
         newToken
      } catch (e: Exception) {
         Log.e(TAG, "Error refreshing token")
         null
      }
   }

   companion object {
      private const val TAG = "IngredientRepositoryImpl"
   }
}