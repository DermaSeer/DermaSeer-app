package com.dermaseer.dermaseer.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {

   private var _binding: FragmentOnBoardingBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      // Inflate the layout for this fragment
      _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      navController = Navigation.findNavController(view)
      binding.btnNext.setOnClickListener { navController.navigate(R.id.action_onBoardingFragment_to_signinFragment) }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}