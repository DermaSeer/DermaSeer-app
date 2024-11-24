package com.dermaseer.dermaseer.ui.complete_profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentCompleteProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class CompleteProfileFragment : Fragment() {

   private var _binding: FragmentCompleteProfileBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController
   private val auth: FirebaseAuth = FirebaseAuth.getInstance()
   private val completeProfileViewModel: CompleteProfileViewModel by viewModels()

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      // Inflate the layout for this fragment
      _binding = FragmentCompleteProfileBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      navController = Navigation.findNavController(view)
      binding.etName.setText(auth.currentUser?.displayName)
      binding.btnSave.setOnClickListener { completeProfile() }
   }

   private fun completeProfile() {
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
            completeProfileViewModel.completeUserData(
               nameToRequestBody,
               birthdayToRequestBody,
               genderToRequestBody,
               profilePictureToRequestBody
            )
            completeProfileViewModel.userData.observe(viewLifecycleOwner) { user ->
               user?.let {
                  if (user.success == true) {
                     try {
                        navController.navigate(R.id.action_completeProfileFragment_to_homeFragment)
                     } catch (e: IllegalArgumentException) {
                        Log.e("NavigationError", "${e.message}")
                     } finally {
                        Snackbar.make(binding.root, "Success", Snackbar.LENGTH_SHORT).show()
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