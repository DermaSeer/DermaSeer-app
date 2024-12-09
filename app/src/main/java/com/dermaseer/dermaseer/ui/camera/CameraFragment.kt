package com.dermaseer.dermaseer.ui.camera

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentCameraBinding
import com.dermaseer.dermaseer.utils.createCustomTempFile

class CameraFragment : Fragment() {

   private var _binding: FragmentCameraBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController
   private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
   private var imageCapture: ImageCapture? = null

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      // Inflate the layout for this fragment
      _binding = FragmentCameraBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      navController = Navigation.findNavController(view)
      startCamera()
      binding.btnTakePhoto.setOnClickListener { takePhoto() }
      binding.icCameraSwitch.setOnClickListener { switchCamera() }

      requireActivity().onBackPressedDispatcher.addCallback {
         findNavController().navigate(R.id.action_cameraFragment_to_homeFragment)
      }
   }

   private fun startCamera() {
      val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

      cameraProviderFuture.addListener({
         val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
         val preview = Preview.Builder()
            .build()
            .also {
               it.surfaceProvider = binding.cameraPreview.surfaceProvider
            }

         imageCapture = ImageCapture.Builder().build()

         try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
               this,
               cameraSelector,
               preview,
               imageCapture
            )

         } catch (exc: Exception) {
            Toast.makeText(
               context,
               "Gagal memunculkan kamera.",
               Toast.LENGTH_SHORT
            ).show()
            Log.e("CameraCheck", "startCamera: ${exc.message}")
         }
      }, ContextCompat.getMainExecutor(requireContext()))
   }

   private fun takePhoto() {
      val photoFile = createCustomTempFile(requireContext())
      val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
      imageCapture?.takePicture(
         outputOptions,
         ContextCompat.getMainExecutor(requireContext()),
         object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
               val savedUri = outputFileResults.savedUri
               if (savedUri != null) {
                  setFragmentResult("cameraRequestKey", Bundle().apply {
                     putParcelable("imageUri", savedUri)
                  })
                  navController.navigate(R.id.photoPreviewFragment)
               }
            }

            override fun onError(exception: ImageCaptureException) {
               Log.e("CameraFragment", "Photo capture failed: ${exception.message}", exception)
            }
         }
      )
   }

   private fun switchCamera() {
      cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
         CameraSelector.DEFAULT_BACK_CAMERA
      } else {
         CameraSelector.DEFAULT_FRONT_CAMERA
      }
      startCamera()
   }
   
      override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}