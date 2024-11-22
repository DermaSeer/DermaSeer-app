package com.dermaseer.dermaseer.ui.profile

import android.content.Context
import android.os.Build
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaseer.dermaseer.data.local.datastore.AuthPreferences
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
   private val authPreferences: AuthPreferences,
   private val auth: FirebaseAuth,
   private val googleSignInClient: GoogleSignInClient
): ViewModel() {

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

   private fun clearSession() {
      viewModelScope.launch {
         authPreferences.clearSession()
      }
   }

   fun isUserSignedOut(): Boolean {
      return auth.currentUser == null
   }
}