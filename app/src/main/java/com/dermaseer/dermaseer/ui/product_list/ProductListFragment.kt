package com.dermaseer.dermaseer.ui.product_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.adapter.ProductListAdapter
import com.dermaseer.dermaseer.databinding.FragmentProductListBinding
import com.dermaseer.dermaseer.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment : Fragment() {

   private var _binding: FragmentProductListBinding? = null
   private val binding get() = _binding!!
   private val productListViewModel: ProductListViewModel by viewModels()

   private val args: ProductListFragmentArgs by navArgs()

   private val productListAdapter = ProductListAdapter()

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentProductListBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      binding.rvProductList.layoutManager = GridLayoutManager(context, 2)
      binding.rvProductList.adapter = productListAdapter

      val selectedCategory = args.selectedCategory

      productListViewModel.getProductList(selectedCategory)

      val navController = findNavController()

      (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

      binding.topAppBar.setNavigationOnClickListener {
         navController.navigate(R.id.action_productListFragment_to_homeFragment)
      }

      binding.rvProductList.setOnClickListener {
         navController.navigate(R.id.action_productListFragment_to_productDetailFragment)
      }

      lifecycleScope.launchWhenStarted {
         productListViewModel.productListState.collect { result ->
            when (result) {
               is ResultState.Loading -> {
                  binding.progressBar.visibility = View.VISIBLE
               }
               is ResultState.Success -> {
                  binding.progressBar.visibility = View.GONE
                  val productList = productListViewModel.productList.value
                  if (productList.isNotEmpty()) {
                     productListAdapter.submitList(productList)
                  } else {
                     Toast.makeText(context, "No products found in this category", Toast.LENGTH_SHORT).show()
                  }
               }
               is ResultState.Error -> {
                  binding.progressBar.visibility = View.GONE
                  Toast.makeText(context, result.errorMessage, Toast.LENGTH_SHORT).show()
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
