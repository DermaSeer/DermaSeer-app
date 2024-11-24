package com.dermaseer.dermaseer.ui.signin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentSigninBinding
import com.dermaseer.dermaseer.utils.SigninState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SigninFragment : Fragment() {

   private var _binding: FragmentSigninBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController
   private lateinit var auth: FirebaseAuth
   private val signinViewModel: SigninViewModel by viewModels()

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
            signinViewModel.signInWithCredentialManager(requireContext())
         } else {
            signinViewModel.signInWithGoogleSignInSDK(requireContext(), requireActivity())
         }
      }
      action()
   }

   private fun action() {
      signinViewModel.signinState.observe(viewLifecycleOwner) { state ->
         when (state) {
            is SigninState.Idle -> {
               // Do nothing
            }
            is SigninState.LaunchIntent -> {
               startActivityForResult(state.intent, RC_SIGN_IN)
            }
            is SigninState.Success -> {
               navigatePage(state.user)
            }
            is SigninState.Error -> {
               Snackbar.make(binding.root, "Sign in Failed, try again!", Snackbar.LENGTH_SHORT).show()
            }
         }
      }
   }

   @Deprecated("Deprecated in Java")
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      super.onActivityResult(requestCode, resultCode, data)
      if (requestCode == RC_SIGN_IN) {
         val task = GoogleSignIn.getSignedInAccountFromIntent(data)
         signinViewModel.handleGoogleSignInResult(task)
      }
   }

   private fun navigatePage(currentUser: FirebaseUser?) {
      if (currentUser != null) {
         signinViewModel.checkUserResponse.observe(viewLifecycleOwner) { user ->
            Log.d("CheckUserData", "${user.success}")
            if (user.success == true) {
               navController.navigate(R.id.action_signinFragment_to_homeFragment)
            } else {
               navController.navigate(R.id.action_signinFragment_to_completeProfileFragment)
            }
            Snackbar.make(binding.root, "Sign in success", Snackbar.LENGTH_SHORT).show()
         }
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