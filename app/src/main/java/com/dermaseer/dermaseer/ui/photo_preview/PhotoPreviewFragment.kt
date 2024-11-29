package com.dermaseer.dermaseer.ui.photo_preview

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
      analyzePhoto()
      binding.topAppBar.setNavigationOnClickListener { showAlertDialog() }
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
      binding.btnRetake.setOnClickListener { navController.navigate(R.id.action_photoPreviewFragment_to_cameraFragment) }
   }

   private fun analyzePhoto() {
      binding.btnAnalyze.setOnClickListener {
         currentImageUri?.let { uri ->
            val action = PhotoPreviewFragmentDirections
               .actionPhotoPreviewFragmentToScanResultFragment(uri.toString())
            navController.navigate(action)
         }
      }
   }

   @SuppressLint("SetTextI18n")
   private fun showAlertDialog() {
      val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
      val inflater = LayoutInflater.from(requireContext())
      val customView = inflater.inflate(R.layout.custom_alert_dialog, null)

      val titleView = customView.findViewById<TextView>(R.id.dialog_title)
      val messageView = customView.findViewById<TextView>(R.id.dialog_message)
      val positiveBtn = customView.findViewById<Button>(R.id.btn_positive)
      val negativeBtn = customView.findViewById<Button>(R.id.btn_negative)

      titleView.text = "Cancel scan"
      messageView.text = "Are you sure you to cancel scan right now?"
      positiveBtn.text = "YES"
      negativeBtn.text = "NO"

      val dialog = builder.setView(customView).create()
      positiveBtn.setOnClickListener {
         navController.navigate(R.id.action_photoPreviewFragment_to_homeFragment)
         dialog.dismiss()
      }
      negativeBtn.setOnClickListener { dialog.dismiss() }
      dialog.window?.setBackgroundDrawable(ColorDrawable(0))
      dialog.show()
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}