package com.dermaseer.dermaseer.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dermaseer.dermaseer.adapter.ArticleAdapter
import com.dermaseer.dermaseer.data.remote.models.ArticleResponse
import com.dermaseer.dermaseer.databinding.FragmentArticleBinding
import com.dermaseer.dermaseer.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : Fragment() {

   private var _binding: FragmentArticleBinding? = null
   private val binding get() = _binding!!
   private val viewModel: ArticleViewModel by viewModels()
   private lateinit var articleAdapter: ArticleAdapter

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentArticleBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      setupRecyclerView()

      viewModel.articleState.observe(viewLifecycleOwner) { result ->
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
         articles?.let { updateRecyclerView(it) }
      }

      viewModel.getArticles()
   }

   private fun setupRecyclerView() {
      articleAdapter = ArticleAdapter()
      binding.rvArticle.apply {
         layoutManager = LinearLayoutManager(context)
         adapter = articleAdapter
      }
   }

   private fun updateRecyclerView(articles: List<ArticleResponse.Data?>) {
      articleAdapter.submitList(articles)
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}

