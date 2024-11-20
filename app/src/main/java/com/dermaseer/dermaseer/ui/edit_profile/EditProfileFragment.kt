package com.dermaseer.dermaseer.ui.edit_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentEditProfileBinding
import com.dermaseer.dermaseer.databinding.FragmentProfileBinding

class EditProfileFragment : Fragment() {

   private var _binding: FragmentEditProfileBinding? = null
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
      _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      navController = Navigation.findNavController(view)
      binding.topAppBar.setNavigationOnClickListener { navController.navigateUp() }
      binding.btnSave.setOnClickListener { navController.navigateUp() }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}