package com.dermaseer.dermaseer.ui.scan_result

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.adapter.ProductRecommendationAdapter
import com.dermaseer.dermaseer.databinding.FragmentScanResultRecomendationBinding
import com.dermaseer.dermaseer.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanResultRecomendationFragment : Fragment() {

    private var _binding: FragmentScanResultRecomendationBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val args: ScanResultRecomendationFragmentArgs by navArgs()
    private val scanResultRecomendationViewModel: ScanResultRecomendationViewModel by viewModels()
    private val productRecommendationAdapter: ProductRecommendationAdapter = ProductRecommendationAdapter()
    private lateinit var resultId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentScanResultRecomendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.topAppBar.setNavigationOnClickListener { navController.navigate(R.id.action_scanResultRecomendationFragment2_to_homeFragment) }
        val image = args.imageUri
        binding.ivRecomendationAnalyze.setImageURI(Uri.parse(image))
    }

    private fun getRecommendations() {
        val predictId = args.predictId
        val skinType = args.skinType
        val productCategory = args.productCategory

        scanResultRecomendationViewModel.fetchAllRecommendations(predictId, skinType, productCategory)
        scanResultRecomendationViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResultState.Loading -> {
                    with(binding) {
                        lottieLoading.visibility = View.VISIBLE
                        scrollView.visibility = View.GONE
                    }
                }
                is ResultState.Success -> {
                    with(binding) {
                        lottieLoading.visibility = View.GONE
                        scrollView.visibility = View.VISIBLE
                        resultRecommendation.text = scanResultRecomendationViewModel.ingredientsResponse.value?.data?.message
                        rvProductRecommendations.apply {
                            adapter = productRecommendationAdapter
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            productRecommendationAdapter.setData(scanResultRecomendationViewModel.productRecommendationResponse.value?.data)
                        }
                    }
                }
                is ResultState.Error -> {
                    with(binding) {
                        lottieLoading.visibility = View.GONE
                        scrollView.visibility - View.VISIBLE
                        resultRecommendation.text = state.message
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}