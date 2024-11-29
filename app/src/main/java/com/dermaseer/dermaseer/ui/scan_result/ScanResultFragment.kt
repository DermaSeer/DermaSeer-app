package com.dermaseer.dermaseer.ui.scan_result

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentScanResultBinding

class ScanResultFragment : Fragment() {

   private var _binding: FragmentScanResultBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController
   private val args: ScanResultFragmentArgs by navArgs()

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
      getRecommendation()
   }

   private fun getRecommendation() {
      val image = args.imageUri
      binding.ivAnalyzeResult.setImageURI(Uri.parse(image))
      binding.btnRecommendation.setOnClickListener {
         val action = ScanResultFragmentDirections
            .actionScanResultFragmentToScanResultRecomendationFragment2(image)
         navController.navigate(action)
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}