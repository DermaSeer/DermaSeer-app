package com.dermaseer.dermaseer.ui.edit_profile

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentEditProfileBinding
import com.dermaseer.dermaseer.databinding.FragmentProfileBinding
import com.dermaseer.dermaseer.utils.ResultState
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
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
      editProfileViewModel.state.observe(viewLifecycleOwner) { state ->
         when(state) {
            is ResultState.Loading -> {
               binding.btnSave.showProgress {
                  progressColor = resources.getColor(R.color.white)
                  buttonTextRes = R.string.please_wait
               }
            }
            is ResultState.Success -> {
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
                                 showStateDialog(R.drawable.check, "Update data success")
                              }
                           }
                        }
                     }
                     binding.btnSave.hideProgress(R.string.save)
                  }
               } else {
                  Snackbar.make(binding.root, "Fill out all fields!", Snackbar.LENGTH_SHORT).show()
               }
            }
            is ResultState.Error -> {
               binding.btnSave.hideProgress(R.string.save)
               showStateDialog(R.drawable.remove, "Edit data failed")
            }
         }
      }
   }

   private fun showStateDialog(
      icon: Int,
      title: String,
   ) {
      val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
      val inflater = LayoutInflater.from(requireContext())
      val customView = inflater.inflate(R.layout.state_dialog, null)

      val iconView = customView.findViewById<ImageView>(R.id.iv_state)
      val titleView = customView.findViewById<TextView>(R.id.dialog_title)
      val btnDismiss = customView.findViewById<Button>(R.id.btn_dismiss)

      titleView.text = title
      iconView.setImageResource(icon)

      val dialog = builder.setView(customView).create()
      btnDismiss.setOnClickListener {
         dialog.dismiss()
      }
      dialog.window?.setBackgroundDrawable(ColorDrawable(0))
      dialog.show()
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}