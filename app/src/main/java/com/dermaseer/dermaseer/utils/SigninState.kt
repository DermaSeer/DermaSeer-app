package com.dermaseer.dermaseer.utils

import android.content.Intent
import com.google.firebase.auth.FirebaseUser

sealed class SigninState {
   data object Idle : SigninState()
   data class LaunchIntent(val intent: Intent) : SigninState()
   data class Success(val user: FirebaseUser?) : SigninState()
   data class Error(val message: String) : SigninState()
}