package com.dermaseer.dermaseer.ui.product_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.adapter.LoadingStateAdapter
import com.dermaseer.dermaseer.adapter.ProductListAdapter
import com.dermaseer.dermaseer.databinding.FragmentProductListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
      binding.rvProductList.adapter = productListAdapter.withLoadStateFooter(
         footer = LoadingStateAdapter { productListAdapter.retry() }
      )

      val selectedCategory = args.selectedCategory

      viewLifecycleOwner.lifecycleScope.launch {
         productListViewModel.getProductList(selectedCategory)
      }

      lifecycleScope.launchWhenStarted {
         productListViewModel.productList?.collectLatest { pagingData ->
            productListAdapter.submitData(pagingData)
         }
      }

      productListAdapter.addLoadStateListener { loadState ->
         binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading

         val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error
            ?: loadState.refresh as? LoadState.Error

         errorState?.let {
            Toast.makeText(
               context,
               it.error.localizedMessage ?: "Failed to load products.",
               Toast.LENGTH_SHORT
            ).show()
         }

         binding.ivNoData.isVisible =
            loadState.source.refresh is LoadState.NotLoading && productListAdapter.itemCount == 0
      }

      binding.topAppBar.title = selectedCategory
      (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

      binding.topAppBar.setNavigationOnClickListener {
         findNavController().navigate(R.id.action_productListFragment_to_homeFragment)
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}

