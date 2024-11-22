package com.dermaseer.dermaseer.ui.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

   private var _binding: FragmentProfileBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController
   private val profileViewModel: ProfileViewModel by viewModels()

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
      binding.btnSignout.setOnClickListener {
         profileViewModel.signOut(
            onSignOutSuccess = { navigatePage() },
            onSignOutFailure = {
               Log.e("TestLogout", "${it?.message}")
            },
            context = requireContext()
         )
      }
      editProfile()
      changeLanguage()
   }

   private fun navigatePage() {
      if (profileViewModel.isUserSignedOut()) {
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

   private fun changeLanguage(){
      binding.cardLanguage.setOnClickListener {
         startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
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