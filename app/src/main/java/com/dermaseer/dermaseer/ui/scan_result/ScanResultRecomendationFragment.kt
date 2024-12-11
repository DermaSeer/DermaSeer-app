package com.dermaseer.dermaseer.ui.scan_result

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.adapter.ProductRecommendationAdapter
import com.dermaseer.dermaseer.data.remote.models.IngredientsRequest
import com.dermaseer.dermaseer.databinding.FragmentScanResultRecomendationBinding
import com.dermaseer.dermaseer.utils.ResultState
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class ScanResultRecomendationFragment : Fragment() {

    private var _binding: FragmentScanResultRecomendationBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val args: ScanResultRecomendationFragmentArgs by navArgs()
    private val scanResultRecomendationViewModel: ScanResultRecomendationViewModel by viewModels()
    private val productRecommendationAdapter: ProductRecommendationAdapter = ProductRecommendationAdapter()
    private var resultId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            resultId = it.getString(KEY_RESULT_ID, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanResultRecomendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.topAppBar.setNavigationOnClickListener { navController.navigate(R.id.action_scanResultRecomendationFragment2_to_homeFragment) }
        val image = args.imageUri
        binding.ivRecomendationAnalyze.setImageURI(Uri.parse(image))
        if (resultId.isEmpty()) {
            getRecommendations()
        } else {
            updateUI()
        }
        getProductDetail()
    }

    private fun getRecommendations() {
        val predictId = args.predictId
        val skinType = args.skinType
        val productCategory = args.productCategory

        val requestBody = createJsonRequestBody(predictId, skinType, productCategory)

        scanResultRecomendationViewModel.fetchAllRecommendations(requestBody)

        viewLifecycleOwner.lifecycleScope.launch {
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
                                resultId = response?.data?.resultId ?: ""
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
                            scrollView.visibility = View.VISIBLE
                            lottieLoading.cancelAnimation()
                            resultRecommendation.text = state.message
                        }
                        showStateDialog(R.drawable.remove, "Something wrong, retry!")
                    }
                }
            }
        }
    }

    private fun updateUI() {
        binding.lottieLoading.visibility = View.GONE
        binding.scrollView.visibility = View.VISIBLE
        scanResultRecomendationViewModel.ingredientsResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                binding.resultRecommendation.text = response.data?.message
                resultId = response.data?.resultId ?: ""
            }
        }
        scanResultRecomendationViewModel.productRecommendationResponse.observe(viewLifecycleOwner) { response ->
            response?.data?.let {
                Log.d("CheckDataResponse", "Data Produk: ${response.data}")
                binding.rvProductRecommendations.apply {
                    adapter = productRecommendationAdapter
                    setHasFixedSize(true)
                    layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
                    productRecommendationAdapter.setData(scanResultRecomendationViewModel.productRecommendationResponse.value?.data)
                }
            }
        }
    }

    private fun createJsonRequestBody(predictId: String, skinType: String, productCategory: String): RequestBody {
        val request = IngredientsRequest(
            predictId = predictId,
            skinType = skinType,
            productCategory = productCategory
        )

        val json = Gson().toJson(request)
        return json.toRequestBody("application/json".toMediaType())
    }

    private fun getProductDetail() {
        productRecommendationAdapter.setOnItemClickCallback(object: ProductRecommendationAdapter.OnItemClickCallback {
            override fun onItemClicked(predictId: String) {
                val toDetail = ScanResultRecomendationFragmentDirections
                    .actionScanResultRecomendationFragment2ToProductDetailRecomendationFragment(predictId, resultId)
                navController.navigate(toDetail)
            }
        })
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
        btnDismiss.text = "Retry"

        val dialog = builder.setView(customView).create()
        btnDismiss.setOnClickListener {
            navController.navigateUp()
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_RESULT_ID, resultId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_RESULT_ID = "resultId"
    }
}
