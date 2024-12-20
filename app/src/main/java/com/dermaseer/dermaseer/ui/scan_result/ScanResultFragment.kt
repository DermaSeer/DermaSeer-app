package com.dermaseer.dermaseer.ui.scan_result

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentScanResultBinding
import com.dermaseer.dermaseer.utils.ResultState
import com.dermaseer.dermaseer.utils.reduceFileImage
import com.dermaseer.dermaseer.utils.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.Q)
class ScanResultFragment : Fragment() {

   private var _binding: FragmentScanResultBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController
   private val args: ScanResultFragmentArgs by navArgs()
   private val scanResultViewModel: ScanResultViewModel by viewModels()
   private var predictId: String = ""
   private var skinType: String = ""
   private var productCategory: String = ""

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      // Inflate the layout for this fragment
      _binding = FragmentScanResultBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      navController = Navigation.findNavController(view)
      requireActivity().onBackPressedDispatcher.addCallback {
         findNavController().navigate(R.id.action_scanResultFragment_to_homeFragment)
      }
      scanPhoto()
      getScanResult()
      getRecommendation()
      setupChipGroup()
   }

   private fun scanPhoto() {
      val imageUri = Uri.parse(args.imageUri)
      val imageFile = uriToFile(imageUri, requireContext())
      imageFile.reduceFileImage()
      val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
      val multipartBody = MultipartBody.Part.createFormData(
         "image",
         imageFile.name,
         requestImageFile
      )
      scanResultViewModel.fetchPrediction(multipartBody)
   }

   private fun getScanResult() {
      scanResultViewModel.state.observe(viewLifecycleOwner) { state ->
         when(state) {
            is ResultState.Loading -> {
               with(binding) {
                  cardImage.visibility = View.GONE
                  cardView.visibility = View.GONE
                  cardView2.visibility = View.GONE
                  lottieLoading.visibility = View.VISIBLE
               }
            }
            is ResultState.Success -> {
               scanResultViewModel.predictResponse.observe(viewLifecycleOwner) { response ->
                  binding.tvAcneType.text = getString(R.string.acne_type_label, response?.data?.acneType ?: "")
                  predictId = response?.data?.predictId ?: ""
               }
               with(binding) {
                  cardImage.visibility = View.VISIBLE
                  cardView.visibility = View.VISIBLE
                  cardView2.visibility = View.VISIBLE
                  lottieLoading.visibility = View.GONE
               }
            }
            is ResultState.Error -> {
               with(binding) {
                  cardImage.visibility = View.VISIBLE
                  cardView.visibility = View.VISIBLE
                  cardView2.visibility = View.VISIBLE
                  lottieLoading.visibility = View.GONE
               }
               showStateDialog(R.drawable.remove, state.message)
            }
         }
      }
   }

   private fun setupChipGroup() {
      binding.chipGroupProduct.setOnCheckedStateChangeListener { _, checkedIds ->
         if (checkedIds.isNotEmpty()) {
            val selectedChipId = checkedIds.first()
            productCategory = when (selectedChipId) {
               R.id.chipSerum -> "Essence Wajah"
               R.id.chipToner -> "Toner"
               R.id.chipFacewash -> "Pembersih Wajah"
               R.id.chipMoisturizer -> "Pelembab Wajah"
               R.id.chipSunscreen -> "Sunscreen Wajah"
               else -> "Essence Wajah"
            }
         } else {
            productCategory = "Essence Wajah"
         }
      }

      binding.chipGroupProblem.setOnCheckedStateChangeListener { _, checkedIds ->
         if (checkedIds.isNotEmpty()) {
            val selectedChipId = checkedIds.first()
            skinType = when (selectedChipId) {
               R.id.chipDry -> "Kering"
               R.id.chipOily -> "Berminyak"
               R.id.chipSensitive -> "Sensitif"
               else -> "Kering"
            }
         } else {
            skinType = "Kering"
         }
      }
   }

   private fun getRecommendation() {
      val image = args.imageUri
      var imageUrl = ""
      binding.ivAnalyzeResult.setImageURI(Uri.parse(image))
      binding.btnRecommendation.setOnClickListener {
         scanResultViewModel.predictResponse.observe(viewLifecycleOwner) { response ->
            imageUrl = response?.data?.imageUrl ?: ""
         }
         val action = ScanResultFragmentDirections
            .actionScanResultFragmentToScanResultRecomendationFragment2(
               imageUri = imageUrl,
               predictId = predictId,
               skinType = skinType,
               productCategory = productCategory
            )
         navController.navigate(action)
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