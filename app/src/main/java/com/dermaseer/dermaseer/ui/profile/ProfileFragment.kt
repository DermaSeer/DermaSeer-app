package com.dermaseer.dermaseer.ui.profile

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

   private var _binding: FragmentProfileBinding? = null
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
      _binding = FragmentProfileBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      navController = Navigation.findNavController(view)
      binding.btnSignout.setOnClickListener { signOut() }
      editProfile()
   }

   private fun signOut() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
         viewLifecycleOwner.lifecycleScope.launch {
            val credentialManager = CredentialManager.create(requireContext())
            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            navigatePage()
         }
      } else {
         val googleSignInClient = GoogleSignIn.getClient(requireContext(), GoogleSignInOptions.DEFAULT_SIGN_IN)
         googleSignInClient.signOut()
            .addOnCompleteListener(requireActivity()) { task ->
               if (task.isSuccessful) {
                  FirebaseAuth.getInstance().signOut()
                  navigatePage()
                  Log.d(TAG, "Signed out from Google")
               } else {
                  Log.e(TAG, "Failed to sign out from Google", task.exception)
               }
            }
      }
   }

   private fun navigatePage() {
      if (FirebaseAuth.getInstance().currentUser == null) {
         navController.navigate(R.id.action_profileFragment_to_onBoardingFragment)
         Snackbar.make(binding.root, "Sign out Success", Snackbar.LENGTH_SHORT).show()
      } else {
         Snackbar.make(binding.root, "Failed to sign out, try again.", Snackbar.LENGTH_SHORT).show()
      }
   }

   private fun editProfile() {
      binding.ivEditProfile.setOnClickListener {
         navController.navigate(R.id.action_profileFragment_to_editProfileFragment)
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }

   companion object {
      const val TAG = "ProfileFragmentSignOut"
   }
}