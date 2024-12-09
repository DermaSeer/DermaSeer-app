package com.dermaseer.dermaseer.ui.profile

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentProfileBinding
import com.dermaseer.dermaseer.utils.ResultState
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
   private val auth = FirebaseAuth.getInstance()

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
         showAlertDialog(
            requireContext().getString(R.string.confirm_sign_out),
            requireContext().getString(R.string.are_you_sure_sign_out),
            requireContext().getString(R.string.yes),
            requireContext().getString(R.string.no),
            onPositiveAction = {
               profileViewModel.signOut(
                  onSignOutSuccess = {
                     navigatePage()
                     showStateDialog(R.drawable.check, requireContext().getString(R.string.sign_out_success))
                  },
                  onSignOutFailure = {
                     showStateDialog(R.drawable.remove, requireContext().getString(R.string.sign_out_failed))
                  },
                  context = requireContext()
               )
            }
         )
      }

      binding.cardDeleteAccount.setOnClickListener {
         showAlertDialog(
            requireContext().getString(R.string.confirm_delete_account),
            requireContext().getString(R.string.are_you_sure_delete_account),
            requireContext().getString(R.string.delete),
            requireContext().getString(R.string.cancel),
            onPositiveAction = {
               viewLifecycleOwner.lifecycleScope.launch {
                  profileViewModel.deleteUserAccount()
                  profileViewModel.deleteUserResponse.observe(viewLifecycleOwner) {
                     showStateDialog(R.drawable.check, requireContext().getString(R.string.delete_account_success))
                  }
                  profileViewModel.signOut(
                     onSignOutSuccess = {
                        navigatePage()
                        showStateDialog(R.drawable.check, requireContext().getString(R.string.delete_account_success))
                     },
                     onSignOutFailure = { showStateDialog(R.drawable.remove, requireContext().getString(R.string.delete_account_failed)) },
                     context = requireContext()
                  )
               }
            }
         )
      }
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
         profileViewModel.state.observe(viewLifecycleOwner) { state ->
            when(state) {
               is ResultState.Loading -> {
                  binding.lottieLoading.visibility = View.VISIBLE
                  binding.lottieLoading.playAnimation()
               }
               is ResultState.Success -> {
                  binding.lottieLoading.visibility = View.GONE
                  binding.lottieLoading.cancelAnimation()
                  profileViewModel.userData.observe(viewLifecycleOwner) { user ->
                     with(binding) {
                        Log.d("CheckPhoto", user.data?.user?.profilePicture.toString())
                        Glide.with(ivUserPhoto)
                           .load(auth.currentUser?.photoUrl)
                           .error(R.drawable.unknownperson)
                           .into(ivUserPhoto)
                        tvUserName.text = user.data?.user?.name
                        tvUserEmail.text = user.data?.user?.email
                        tvUserAge.text = getString(R.string.age_format, user.data?.user?.age)
                        tvUserGender.text = user.data?.user?.gender
                     }
                  }
               }
               is ResultState.Error -> {
                  binding.lottieLoading.visibility = View.GONE
                  binding.lottieLoading.cancelAnimation()
                  showStateDialog(R.drawable.remove, "Error! please close the app")
               }
            }
         }
      }
   }

   private fun showAlertDialog(
      title: String,
      message: String,
      positiveButton: String,
      negativeButton: String,
      onPositiveAction: () -> Unit
   ) {
      val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
      val inflater = LayoutInflater.from(requireContext())
      val customView = inflater.inflate(R.layout.custom_alert_dialog, null)

      val titleView = customView.findViewById<TextView>(R.id.dialog_title)
      val messageView = customView.findViewById<TextView>(R.id.dialog_message)
      val positiveBtn = customView.findViewById<Button>(R.id.btn_positive)
      val negativeBtn = customView.findViewById<Button>(R.id.btn_negative)

      titleView.text = title
      messageView.text = message
      positiveBtn.text = positiveButton
      negativeBtn.text = negativeButton

      val dialog = builder.setView(customView).create()
      positiveBtn.setOnClickListener {
         onPositiveAction()
         dialog.dismiss()
      }
      negativeBtn.setOnClickListener {
         dialog.dismiss()
      }
      dialog.window?.setBackgroundDrawable(ColorDrawable(0))
      dialog.show()
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