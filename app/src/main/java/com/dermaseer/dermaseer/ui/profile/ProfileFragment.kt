package com.dermaseer.dermaseer.ui.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentProfileBinding
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
            onSignOutSuccess = {
               navigatePage()
               Snackbar.make(binding.root, "Sign out Success", Snackbar.LENGTH_SHORT).show()
            },
            onSignOutFailure = {
               Log.e("TestLogout", "${it?.message}")
            },
            context = requireContext()
         )
      }
      binding.cardDeleteAccount.setOnClickListener { showAlertDialog() }
      getUserData()
      editProfile()
      changeLanguage()
   }

   private fun navigatePage() {
      if (profileViewModel.isUserSignedOut()) {
         navController.navigate(R.id.action_profileFragment_to_onBoardingFragment)
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

   private fun getUserData() {
      viewLifecycleOwner.lifecycleScope.launch {
         profileViewModel.userData.observe(viewLifecycleOwner) { user ->
            with(binding) {
               Log.d("CheckPhoto", user.data?.user?.profilePicture.toString())
               Glide.with(ivUserPhoto)
                  .load(user.data?.user?.profilePicture)
                  .error(R.drawable.unknownperson)
                  .into(ivUserPhoto)
               tvUserName.text = user.data?.user?.name
               tvUserEmail.text = user.data?.user?.email
               tvUserAge.text = user.data?.user?.birthday
               tvUserGender.text = user.data?.user?.gender
            }
         }
      }
   }

   private fun showAlertDialog() {
      val builder = AlertDialog.Builder(requireContext())
      builder.setTitle("Konfirmasi hapus akun")
      builder.setMessage("Semua data Anda akan dihapus secara permanen.")
      builder.setPositiveButton("Ya") { _, _ ->
         viewLifecycleOwner.lifecycleScope.launch {
            profileViewModel.deleteUserAccount()
            profileViewModel.deleteUserResponse.observe(viewLifecycleOwner) { response ->
               Snackbar.make(binding.root, "${response.message}", Snackbar.LENGTH_SHORT).show()
            }
            profileViewModel.signOut(
               onSignOutSuccess = { navigatePage() },
               onSignOutFailure = {  },
               context = requireContext()
            )
         }
      }
      builder.setNegativeButton("Tidak") { dialog, _ ->
         dialog.dismiss()
      }
      builder.create().show()
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}