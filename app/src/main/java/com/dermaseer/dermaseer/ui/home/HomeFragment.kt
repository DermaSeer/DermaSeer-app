package com.dermaseer.dermaseer.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.adapter.ArticleHomeAdapter
import com.dermaseer.dermaseer.data.remote.models.ArticleResponse
import com.dermaseer.dermaseer.databinding.FragmentHomeBinding
import com.dermaseer.dermaseer.databinding.ItemProductTypeBinding
import com.dermaseer.dermaseer.ui.article.ArticleViewModel
import com.dermaseer.dermaseer.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {

   private var _binding: FragmentHomeBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController
   private val viewModel: ArticleViewModel by viewModels()
   private lateinit var articleHomeAdapter: ArticleHomeAdapter

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      // Inflate the layout for this fragment
      _binding = FragmentHomeBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      navController = Navigation.findNavController(view)
      setProductType()
      setupRecyclerViewArticle()

      viewModel.articleState.observe(viewLifecycleOwner){ result ->
         when (result) {
            is ResultState.Loading -> binding.progressBar.visibility = View.VISIBLE
            is ResultState.Success -> {
               binding.progressBar.visibility = View.GONE
               Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
            is ResultState.Error -> {
               binding.progressBar.visibility = View.GONE
               Toast.makeText(context, result.errorMessage, Toast.LENGTH_SHORT).show()
            }
         }
      }
      viewModel.articles.observe(viewLifecycleOwner) { articles ->
         articles?.let { updateRecyclerViewArticle(it) }
      }
      viewModel.getArticles()
   }

   private fun setupRecyclerViewArticle() {
      articleHomeAdapter = ArticleHomeAdapter()
      binding.rvArticleHome.apply {
         layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
         adapter = articleHomeAdapter
      }
   }

   private fun updateRecyclerViewArticle(articles: List<ArticleResponse.Data?>) {
      val sortedArticles = articles
         .filterNotNull()
         .sortedByDescending { article ->
            article.publishDate?.toString()?.let { publishDateStr ->
               try {
                  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(publishDateStr)
               } catch (e: Exception) {
                  null
               }
            }
         }
         .take(3)

      articleHomeAdapter.submitList(sortedArticles)
   }

   private fun setProductType() {
      val listProductType = listOf(
         ProductType(R.drawable.moisturizer, requireContext().getString(R.string.moisturizer)),
         ProductType(R.drawable.toner, requireContext().getString(R.string.toner)),
         ProductType(R.drawable.serum, requireContext().getString(R.string.serum)),
         ProductType(R.drawable.facewash, requireContext().getString(R.string.facewash)),
         ProductType(R.drawable.sunscreen, requireContext().getString(R.string.sunscreen)),
      )

      val listCard: List<ItemProductTypeBinding>
      with (binding) {
         listCard = listOf(cardMoisturizer, cardToner, cardSerum, cardFacewash, cardSunscreen)
      }

      for (i in listProductType.indices) {
         listCard[i].tvProductType.text = listProductType[i].productTitle
         listCard[i].tvProductImage.setImageResource(listProductType[i].productImage)

         listCard[i].root.setOnClickListener {
            val selectedCategory = listProductType[i].productTitle
            val action = HomeFragmentDirections.actionHomeFragmentToProductListFragment(selectedCategory)
            navController.navigate(action)
         }
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}

data class ProductType (
   val productImage: Int,
   val productTitle: String
)