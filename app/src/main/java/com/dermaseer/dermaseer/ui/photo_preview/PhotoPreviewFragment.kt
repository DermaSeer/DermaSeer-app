package com.dermaseer.dermaseer.ui.photo_preview

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentPhotoPreviewBinding

class PhotoPreviewFragment : Fragment() {

   private var _binding: FragmentPhotoPreviewBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController
   private var currentImageUri: Uri? = null

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      // Inflate the layout for this
      _binding = FragmentPhotoPreviewBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      navController = Navigation.findNavController(view)
      retakePhoto()
      binding.topAppBar.setNavigationOnClickListener { navController.navigate(R.id.action_photoPreviewFragment_to_homeFragment) }
      parentFragmentManager.setFragmentResultListener("cameraRequestKey", viewLifecycleOwner) { _, bundle ->
         val uri = bundle.getParcelable<Uri>("imageUri")
         if (uri != null) {
            currentImageUri = uri
            showImage()
            Log.d("CheckCurrentImage", "Check if not null: $currentImageUri")
         }
      }
   }

   private fun showImage() {
      currentImageUri?.let {
         binding.ivPreview.setImageURI(currentImageUri)
      }
   }

   private fun retakePhoto() {
      binding.btnRetake.setOnClickListener { navController.navigateUp() }
   }

   private fun analyzePhoto() {
      // Wait until scan result UI is done
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}