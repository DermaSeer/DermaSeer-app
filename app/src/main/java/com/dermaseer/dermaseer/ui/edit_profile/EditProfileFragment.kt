package com.dermaseer.dermaseer.ui.edit_profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentEditProfileBinding
import com.dermaseer.dermaseer.databinding.FragmentProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

   private var _binding: FragmentEditProfileBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController
   private val editProfileViewModel: EditProfileViewModel by viewModels()
   private val auth = FirebaseAuth.getInstance()

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      // Inflate the layout for this fragment
      _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      navController = Navigation.findNavController(view)
      getUserData()
      binding.topAppBar.setNavigationOnClickListener { navController.navigateUp() }
      binding.btnSave.setOnClickListener { updateUserData() }
   }

   private fun getUserData() {
      viewLifecycleOwner.lifecycleScope.launch {
         editProfileViewModel.userData.observe(viewLifecycleOwner) { user ->
            with(binding) {
               Glide.with(profileImage)
                  .load(auth.currentUser?.photoUrl)
                  .error(R.drawable.unknownperson)
                  .into(profileImage)
               etName.setText("${user.data?.user?.name}")
               etEmail.setText("${user.data?.user?.email}")
               etAge.setText("${user.data?.user?.birthday}")
               if (user.data?.user?.gender == "Male") {
                  radioMan.isChecked = true
               } else {
                  radioWoman.isChecked = true
               }
            }
         }
      }
   }

   private fun updateUserData() {
      val name = binding.etName.text.toString()
      val birthday = binding.etAge.text.toString()
      val gender = if (binding.radioMan.isChecked) {
         "Male"
      } else if (binding.radioWoman.isChecked){
         "Female"
      } else {
         ""
      }
      val profilePicture = auth.currentUser?.photoUrl.toString()
      if (name.isNotEmpty() && birthday.isNotEmpty() && gender.isNotEmpty()) {
         val nameToRequestBody = name.toRequestBody("text/plain".toMediaType())
         val birthdayToRequestBody = birthday.toRequestBody("text/plain".toMediaType())
         val genderToRequestBody = gender.toRequestBody("text/plain".toMediaType())
         val profilePictureToRequestBody = profilePicture.toRequestBody("text/plain".toMediaType())
         viewLifecycleOwner.lifecycleScope.launch {
            editProfileViewModel.updateUserData(
               nameToRequestBody,
               birthdayToRequestBody,
               genderToRequestBody,
               profilePictureToRequestBody
            )
            editProfileViewModel.newUserData.observe(viewLifecycleOwner) { user ->
               user?.let {
                  if (user.success == true) {
                     try {
                        navController.navigate(R.id.action_editProfileFragment_to_profileFragment)
                     } catch (e: IllegalArgumentException) {
                        Log.e("NavigationError", "${e.message}")
                     } finally {
                        Snackbar.make(binding.root, "Update data success", Snackbar.LENGTH_SHORT).show()
                     }
                  }
               }
            }
         }
      } else {
         Snackbar.make(binding.root, "Fill out all fields!", Snackbar.LENGTH_SHORT).show()
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}