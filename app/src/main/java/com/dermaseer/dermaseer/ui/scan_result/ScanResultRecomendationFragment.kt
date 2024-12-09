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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.adapter.ProductRecommendationAdapter
import com.dermaseer.dermaseer.databinding.FragmentScanResultRecomendationBinding
import com.dermaseer.dermaseer.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

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
        getRecommendations()
        getProductDetail()
    }

    private fun getRecommendations() {
        val predictId = args.predictId
        val skinType = args.skinType
        val productCategory = args.productCategory

        val predictIdToRequestBody = predictId.toRequestBody("text/plain".toMediaType())
        val skinTypeToRequestBody = skinType.toRequestBody("text/plain".toMediaType())
        val productCategoryToRequestBody = productCategory.toRequestBody("text/plain".toMediaType())

        scanResultRecomendationViewModel.fetchAllRecommendations(predictIdToRequestBody, skinTypeToRequestBody, productCategoryToRequestBody)

        scanResultRecomendationViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResultState.Loading -> {
                    with(binding) {
                        lottieLoading.visibility = View.VISIBLE
                        lottieLoading.playAnimation()
                        scrollView.visibility = View.GONE
                    }
                }
                is ResultState.Success -> {
                    with(binding) {
                        lottieLoading.visibility = View.GONE
                        lottieLoading.cancelAnimation()
                        scrollView.visibility = View.VISIBLE
                        scanResultRecomendationViewModel.ingredientsResponse.observe(viewLifecycleOwner) { response ->
                            resultRecommendation.text = response?.data?.message
                        }
                        rvProductRecommendations.apply {
                            adapter = productRecommendationAdapter
                            setHasFixedSize(true)
                            layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
                            productRecommendationAdapter.setData(scanResultRecomendationViewModel.productRecommendationResponse.value?.data)
                        }
                    }
                }
                is ResultState.Error -> {
                    with(binding) {
                        lottieLoading.visibility = View.GONE
                        scrollView.visibility - View.VISIBLE
                        lottieLoading.cancelAnimation()
                        resultRecommendation.text = state.message
                    }
                }
            }
        }
    }

    private fun getProductDetail() {
        productRecommendationAdapter.setOnItemClickCallback(object: ProductRecommendationAdapter.OnItemClickCallback {
            override fun onItemClicked(predictId: String) {
                val toDetail = ScanResultRecomendationFragmentDirections.actionScanResultRecomendationFragment2ToProductDetailRecomendationFragment(predictId)
                navController.navigate(toDetail)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}