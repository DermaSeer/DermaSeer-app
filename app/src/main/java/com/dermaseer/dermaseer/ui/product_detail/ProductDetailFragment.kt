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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.data.remote.models.ProductResponse
import com.dermaseer.dermaseer.databinding.FragmentProductDetailBinding
import com.dermaseer.dermaseer.utils.ResultState
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

   private var _binding: FragmentProductDetailBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController
   private val args: ProductDetailFragmentArgs by navArgs()
   private val viewModel: ProductDetailViewModel by viewModels()

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
      return binding.root
   }

   @SuppressLint("DefaultLocale", "SetTextI18n")
   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      navController = Navigation.findNavController(view)
      binding.topAppBar.setNavigationOnClickListener { navController.navigateUp() }
      requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { navController.navigateUp()}
      binding.btnBuy.setOnClickListener {
         val productJson = args.product
         val product = Gson().fromJson(productJson, ProductResponse.Data::class.java)

         product.url?.let { url ->
            val validUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
               "https://$url"
            } else {
               url
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(validUrl))
            try {
               startActivity(intent)
            } catch (e: Exception) {
               Toast.makeText(requireContext(), requireContext().getString(R.string.unable_open_link), Toast.LENGTH_SHORT).show()
            }
         }
      }

      val productJson = args.product
      viewModel.fetchProductDetails()

      viewModel.productDetails.observe(viewLifecycleOwner) { resultState ->
         when (resultState) {
            is ResultState.Loading -> {
               binding.progressBar.visibility = View.VISIBLE
            }
            is ResultState.Success -> {
               binding.progressBar.visibility = View.GONE
               val product = Gson().fromJson(productJson, ProductResponse.Data::class.java)

               binding.tvProductName.text = product.name
               binding.tvProductStore.text = product.shopName
               binding.tvProductDescription.text = product.description?.replace("\\n", "\n")
               val formattedPrice = NumberFormat.getNumberInstance(Locale("id", "ID")).format(product.price)
               binding.tvProductPrice.text = "Rp$formattedPrice"
               binding.tvProductRating.text = String.format("%.1f", product.productRating)

               Glide.with(this)
                  .load(product.imageUrl)
                  .placeholder(R.drawable.noimage)
                  .into(binding.ivProduct)
            }
            is ResultState.Error -> {
               binding.progressBar.visibility = View.GONE
            }
         }
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}
