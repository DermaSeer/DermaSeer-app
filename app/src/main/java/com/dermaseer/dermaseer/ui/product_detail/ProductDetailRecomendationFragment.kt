package com.dermaseer.dermaseer.ui.product_detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.adapter.MatchedIngredientsAdapter
import com.dermaseer.dermaseer.data.remote.models.ProductRecommendationResponse
import com.dermaseer.dermaseer.databinding.FragmentProductDetailRecomendationBinding
import com.dermaseer.dermaseer.utils.ResultState
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class ProductDetailRecomendationFragment : Fragment() {

    private var _binding: FragmentProductDetailRecomendationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailRecomendationViewModel by viewModels()
    private val args: ProductDetailRecomendationFragmentArgs by navArgs()
    private lateinit var navController: NavController

    private lateinit var ingredientsAdapter: MatchedIngredientsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailRecomendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        binding.topAppBar.setNavigationOnClickListener { navController.navigateUp() }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { navController.navigateUp() }

        setupRecyclerViewIngredient()
        lifecycleScope.launch {
            viewModel.fetchProductDetailRecommendation(args.predictId, args.resultId)
        }

        observeProductDetailRecommendation()
        buyProduct()
    }

    private fun buyProduct() {
        binding.btnBuy.setOnClickListener {
            viewModel.productRecommendation.observe(viewLifecycleOwner) { response ->
                val url = response?.data?.find { it?.id == args.predictId }
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url?.url))
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.unable_open_link),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupRecyclerViewIngredient() {
        ingredientsAdapter = MatchedIngredientsAdapter()
        val flexboxLayoutManager = FlexboxLayoutManager(context).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        binding.rvIngredients.layoutManager = flexboxLayoutManager
        binding.rvIngredients.adapter = ingredientsAdapter
    }

    private fun observeProductDetailRecommendation() {
        viewModel.productDetailRecommendation.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> showLoading(false)
                is ResultState.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.productRecommendation.observe(viewLifecycleOwner) { productRecommendationResponse ->
            productRecommendationResponse?.let { updateUI(it) }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun updateUI(product: ProductRecommendationResponse) {
        val productData = product.data?.find { it?.id == args.predictId }
        productData?.let { data ->
            binding.tvProductName.text = data.name
            binding.tvProductStore.text = data.shopName
            binding.tvProductDescription.text = data.description?.replace("\\n", "\n")
            binding.tvProductPrice.text = formatPrice(data.price)
            binding.tvProductRating.text = String.format("%.1f", data.productRating)

            Glide.with(this)
                .load(data.imageUrl)
                .placeholder(R.drawable.noimage)
                .into(binding.ivProduct)

            ingredientsAdapter.submitList(data.matchedIngredients ?: emptyList())
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.apply {
            visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                playAnimation()
                binding.icStar.visibility = View.GONE
                binding.icStore.visibility = View.GONE
                binding.tvMathcedIngredients.visibility = View.GONE
                binding.tvProductDescriptionTitle.visibility = View.GONE
                binding.btnBuy.isEnabled = false
            } else {
                cancelAnimation()
                binding.icStar.visibility = View.VISIBLE
                binding.icStore.visibility = View.VISIBLE
                binding.tvMathcedIngredients.visibility = View.VISIBLE
                binding.tvProductDescriptionTitle.visibility = View.VISIBLE
                binding.btnBuy.isEnabled = true
            }
        }
    }


    private fun formatPrice(price: Int?): String {
        val formattedPrice = price?.let {
            NumberFormat.getNumberInstance(Locale("id", "ID")).format(it)
        }
        return "Rp$formattedPrice"
    }

    private fun formatUrl(url: String): String {
        return if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "https://$url"
        } else {
            url
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
