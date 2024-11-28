package com.dermaseer.dermaseer.ui.profile

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaseer.dermaseer.data.local.datastore.AuthPreferences
import com.dermaseer.dermaseer.data.remote.models.DeleteUserResponse
import com.dermaseer.dermaseer.data.remote.models.UserResponse
import com.dermaseer.dermaseer.data.repository.user.UserRepository
import com.dermaseer.dermaseer.utils.ResultState
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
   private val authPreferences: AuthPreferences,
   private val auth: FirebaseAuth,
   private val googleSignInClient: GoogleSignInClient,
   private val userRepository: UserRepository
): ViewModel() {

   private var _state = MutableLiveData<ResultState>()
   val state: LiveData<ResultState> = _state

   private var _userData = MutableLiveData<UserResponse>()
   val userData: LiveData<UserResponse> = _userData

   private var _deleteUserResponse = MutableLiveData<DeleteUserResponse>()
   val deleteUserResponse: LiveData<DeleteUserResponse> = _deleteUserResponse

   init {
      getUserData()
   }

   private fun getUserData() {
      viewModelScope.launch {
         _state.value = ResultState.Loading
         try {
            _userData.value = userRepository.getCurrentUser()
            _state.value = ResultState.Success("Success get user data")
         } catch (e: HttpException) {
            _state.value = ResultState.Error(e.message())
            Log.e("GetUser", "Error")
         }
      }
   }

   fun signOut(onSignOutSuccess: () -> Unit, onSignOutFailure: (Exception?) -> Unit, context: Context) {
      viewModelScope.launch {
         try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
               val credentialManager = CredentialManager.create(context)
               auth.signOut()
               clearSession()
               onSignOutSuccess()
               credentialManager.clearCredentialState(ClearCredentialStateRequest())
            } else {
               googleSignInClient.signOut().addOnCompleteListener { task ->
                  if (task.isSuccessful) {
                     auth.signOut()
                     clearSession()
                     onSignOutSuccess()
                  } else {
                     throw task.exception ?: Exception("Sign-out failed")
                  }
               }
            }
         } catch (e: Exception) {
            onSignOutFailure(e)
         }
      }
   }

   fun deleteUserAccount() {
      viewModelScope.launch(Dispatchers.IO) {
         try {
            _deleteUserResponse.value = userRepository.deleteUser()
         } catch (e: HttpException) {
            Log.e("DeleteError", e.message())
         }
      }
   }

   private fun clearSession() {
      viewModelScope.launch {
         authPreferences.clearSession()
      }
   }

   fun isUserSignedOut(): Boolean {
      return auth.currentUser == null
   }
}