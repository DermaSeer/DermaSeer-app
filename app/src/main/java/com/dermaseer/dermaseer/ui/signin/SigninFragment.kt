package com.dermaseer.dermaseer.ui.signin

import android.content.Intent
import android.credentials.GetCredentialException
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentSigninBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class SigninFragment : Fragment() {

   private var _binding: FragmentSigninBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController
   private lateinit var auth: FirebaseAuth

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      // Inflate the layout for this fragment
      _binding = FragmentSigninBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      navController = Navigation.findNavController(view)
      auth = Firebase.auth
      binding.btnSignin.setOnClickListener {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            signInWithCredentialManager()
         } else {
            signInWithGoogleSignInSDK()
         }
      }
   }

   // For Android 34+
   @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
   private fun signInWithCredentialManager() {
      val credentialManager = CredentialManager.create(requireContext())

      val googleOption = GetGoogleIdOption.Builder()
         .setFilterByAuthorizedAccounts(false)
         .setServerClientId(getString(R.string.your_web_client_id))
         .build()

      val request = GetCredentialRequest.Builder()
         .addCredentialOption(googleOption)
         .build()

      viewLifecycleOwner.lifecycleScope.launch {
         try {
            val result: GetCredentialResponse = credentialManager.getCredential(
               request = request,
               context = requireContext(),
            )
            handleSignIn(result)
         } catch (e: GetCredentialException) {
            Log.d("Error", e.message.toString())
         }
      }
   }

   private fun handleSignIn(result: GetCredentialResponse) {
      when (val credential = result.credential) {
         is CustomCredential -> {
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
               try {
                  val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                  firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
               } catch (e: GoogleIdTokenParsingException) {
                  Log.e(TAG, "Received an invalid google id token response", e)
               }
            } else {
               Log.e(TAG, "Unexpected type of credential")
            }
         }
         else -> {
            Log.e(TAG, "Unexpected type of credential")
         }
      }
   }

   // For Android < 34
   private fun signInWithGoogleSignInSDK() {
      val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
         .requestIdToken(getString(R.string.your_web_client_id))
         .requestEmail()
         .build()

      val googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
      startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
   }

   @Deprecated("Deprecated in Java")
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      super.onActivityResult(requestCode, resultCode, data)
      if (requestCode == RC_SIGN_IN) {
         val task = GoogleSignIn.getSignedInAccountFromIntent(data)
         try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
         } catch (e: ApiException) {
            Log.e(TAG, "Google Sign-In failed: ${e.message}")
            Toast.makeText(requireContext(), "Sign-in failed", Toast.LENGTH_SHORT).show()
         }
      }
   }

   private fun firebaseAuthWithGoogle(idToken: String) {
      val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
      auth.signInWithCredential(credential)
         .addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
               Log.d(TAG, "signInWithCredential:success")
               val user: FirebaseUser? = auth.currentUser
               navigatePage(user)
            } else {
               Log.w(TAG, "signInWithCredential:failure", task.exception)
               navigatePage(null)
            }
         }
   }

   private fun navigatePage(currentUser: FirebaseUser?) {
      if (currentUser != null) {
         navController.navigate(R.id.action_signinFragment_to_completeProfileFragment)
         Snackbar.make(binding.root, "Sign in success", Snackbar.LENGTH_SHORT).show()
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }

   companion object {
      const val TAG = "SigninFragment"
      const val RC_SIGN_IN = 100
   }
}