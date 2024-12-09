package com.dermaseer.dermaseer.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaseer.dermaseer.data.remote.models.UserResponse
import com.dermaseer.dermaseer.data.repository.user.UserRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
   private val userRepository: UserRepository
): ViewModel() {
   private var _checkUserResponse = MutableLiveData<UserResponse>()
   val checkUserResponse: LiveData<UserResponse> = _checkUserResponse

   init {
      checkCurrentUser()
   }

   private fun checkCurrentUser() {
      viewModelScope.launch {
         try {
            _checkUserResponse.value = userRepository.getCurrentUser()
         } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = errorBody?.let {
               Gson().fromJson(it, UserResponse::class.java)
            } ?: UserResponse(success = false, data = null, message = null)
            _checkUserResponse.value = errorResponse
            Log.e("LoginError", "HTTP error: ${e.message}")
         } catch (e: SocketTimeoutException) {
            Log.e("Timeout", "${e.message}")
         }
      }
   }
}