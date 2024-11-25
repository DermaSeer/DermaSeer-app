package com.dermaseer.dermaseer.ui.product_detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.data.remote.models.ProductResponse
import com.dermaseer.dermaseer.databinding.FragmentProductDetailBinding
import com.dermaseer.dermaseer.utils.ResultState
import com.google.gson.Gson
import java.text.NumberFormat
import java.util.Locale

class ProductDetailFragment : Fragment() {

   private var _binding: FragmentProductDetailBinding? = null
   private val binding get() = _binding!!

   private val args: ProductDetailFragmentArgs by navArgs()
   private val viewModel: ProductDetailViewModel by viewModels()

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
      return binding.root
   }

   @SuppressLint("DefaultLocale")
   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

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
               Toast.makeText(requireContext(), "Tidak dapat membuka tautan", Toast.LENGTH_SHORT).show()
            }
         }
      }

      val productJson = args.product
      viewModel.fetchProductDetails(productJson)

      viewModel.productDetails.observe(viewLifecycleOwner, Observer { resultState ->
         when (resultState) {
            is ResultState.Loading -> {
               binding.progressBar.visibility = View.VISIBLE
            }
            is ResultState.Success -> {
               binding.progressBar.visibility = View.GONE
               val product = Gson().fromJson(productJson, ProductResponse.Data::class.java)

               binding.productName.text = product.name
               binding.productDescription.text = product.description?.replace("\\n", "\n")
               binding.productPrice.text = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(product.price)
               binding.productRating.text = String.format("%.1f", product.productRating)

               Glide.with(this)
                  .load(product.imageUrl)
                  .placeholder(R.drawable.noimage)
                  .into(binding.imageView)
            }
            is ResultState.Error -> {
               binding.progressBar.visibility = View.GONE
            }
         }
      })
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}
