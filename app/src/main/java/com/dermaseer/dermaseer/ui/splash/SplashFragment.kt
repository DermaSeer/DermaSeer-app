package com.dermaseer.dermaseer.ui.splash

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

   private var _binding: FragmentSplashBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController
   private lateinit var auth: FirebaseAuth
   private val splashViewModel: SplashViewModel by viewModels()

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      // Inflate the layout for this fragment
      _binding = FragmentSplashBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      navController = Navigation.findNavController(view)
      auth = FirebaseAuth.getInstance()
      binding.ivLogo.playAnimation()
      viewLifecycleOwner.lifecycleScope.launch {
         delay(2000)
         splashViewModel.isTimeOut.observe(viewLifecycleOwner) { response ->
            if (response) {
               showStateDialog(R.drawable.remove, "Timeout, try again!")
            } else {
               navigatePage()
            }
         }
      }
   }

   private fun navigatePage() {
      if (isAdded) {
         if (auth.currentUser != null) {
            splashViewModel.checkUserResponse.observe(viewLifecycleOwner) { user ->
               if (user.success == true) navController.navigate(R.id.action_splashFragment_to_homeFragment)
               else navController.navigate(R.id.action_splashFragment_to_completeProfileFragment)
            }
         } else {
            navController.navigate(R.id.action_splashFragment_to_onBoardingFragment)
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
      btnDismiss.text = "Close"

      val dialog = builder.setView(customView).create()
      btnDismiss.setOnClickListener {
         requireActivity().finish()
         dialog.dismiss()
      }
      dialog.window?.setBackgroundDrawable(ColorDrawable(0))
      dialog.show()
   }

   override fun onDestroyView() {
      super.onDestroyView()
      binding.ivLogo.cancelAnimation()
      _binding = null
   }
}