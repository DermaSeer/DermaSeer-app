package com.dermaseer.dermaseer.ui.signin

import android.app.Activity
import android.content.Context
import android.credentials.GetCredentialException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.data.local.datastore.AuthPreferences
import com.dermaseer.dermaseer.data.remote.models.UserResponse
import com.dermaseer.dermaseer.data.repository.user.UserRepository
import com.dermaseer.dermaseer.ui.signin.SigninFragment.Companion.TAG
import com.dermaseer.dermaseer.utils.SigninState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SigninViewModel @Inject constructor(
   private val authPreferences: AuthPreferences,
   private val auth: FirebaseAuth,
   private val credentialManager: CredentialManager,
   private val userRepository: UserRepository
): ViewModel() {
   private var _signinState = MutableLiveData<SigninState>()
   val signinState: LiveData<SigninState> = _signinState

   private var _userData = MutableLiveData<UserResponse>()
   val userData: LiveData<UserResponse> = _userData

   private fun checkCurrentUser() {
      viewModelScope.launch {
         try {
            _userData.value = userRepository.getCurrentUser()
         } catch (e: HttpException) {
            Log.e("GetUser", e.message())
         }
      }
   }

   @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
   fun signInWithCredentialManager(context: Context) {
      viewModelScope.launch {
        try {
           val credentialManager = androidx.credentials.CredentialManager.create(context)

           val googleOption = GetGoogleIdOption.Builder()
              .setFilterByAuthorizedAccounts(false)
              .setServerClientId(context.getString(R.string.your_web_client_id))
              .build()

           val request = GetCredentialRequest.Builder()
              .addCredentialOption(googleOption)
              .build()

           val result = credentialManager.getCredential(context, request)
           val idToken = extractToken(result)
           firebaseAuthWithGoogle(idToken)
        } catch (e: GetCredentialException) {
           _signinState.value = SigninState.Error(e.message ?: "Unknown error")
        }
      }
   }

   fun signInWithGoogleSignInSDK(context: Context, activity: Activity) {
      val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
         .requestIdToken(context.getString(R.string.your_web_client_id))
         .requestEmail()
         .build()

      val googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions)
      _signinState.value = SigninState.LaunchIntent(googleSignInClient.signInIntent)
   }

   fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
      try {
         val account = task.getResult(ApiException::class.java)
         firebaseAuthWithGoogle(account.idToken!!)
      } catch (e: ApiException) {
         _signinState.value = SigninState.Error(e.message ?: "Sign-in failed")
      }
   }

   private fun firebaseAuthWithGoogle(idToken: String) {
      val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
      auth.signInWithCredential(credential)
         .addOnCompleteListener { task ->
            if (task.isSuccessful) {
               Log.d(TAG, "signInWithCredential:success")
               val user: FirebaseUser? = auth.currentUser
               user?.getIdToken(true)?.addOnSuccessListener { result ->
                  val refreshToken = result.token
                  saveToken(refreshToken)
               }
               _signinState.value = SigninState.Success(user)
            } else {
               Log.w(TAG, "signInWithCredential:failure", task.exception)
            }
         }
   }

   private fun saveToken(refreshToken: String?) {
      viewModelScope.launch {
         authPreferences.saveAuthToken(refreshToken)
         checkCurrentUser()
      }
   }

   private fun extractToken(response: GetCredentialResponse): String {
      val googleCredential = response.credential as? GoogleIdTokenCredential
      return googleCredential?.idToken ?: throw IllegalStateException("Token not found")
   }
}