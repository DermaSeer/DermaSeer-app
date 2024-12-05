package com.dermaseer.dermaseer.ui.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.adapter.ArticleAdapter
import com.dermaseer.dermaseer.adapter.LoadingStateAdapter
import com.dermaseer.dermaseer.databinding.FragmentArticleBinding
import com.dermaseer.dermaseer.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private val articleViewModel: ArticleViewModel by viewModels()

    private val articlePagingAdapter = ArticleAdapter { url -> openArticleUrl(url) }

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
        observeArticleList()
        observeResultState()
    }

    private fun setupRecyclerView() {
        binding.rvArticle.layoutManager = LinearLayoutManager(context)
        binding.rvArticle.adapter = articlePagingAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter { articlePagingAdapter.retry() }
        )

        articlePagingAdapter.addLoadStateListener { loadState ->
            binding.lottieLoading.isVisible = loadState.source.refresh is LoadState.Loading
            binding.lottieLoading.playAnimation()

            val errorState = loadState.source.refresh as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    context,
                    it.error.localizedMessage ?: "Failed to load articles.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            binding.ivNoData.isVisible = loadState.source.refresh is LoadState.NotLoading && articlePagingAdapter.itemCount == 0
        }
        binding.lottieLoading.cancelAnimation()
    }

    private fun observeArticleList() {
        lifecycleScope.launch {
            if (articleViewModel.articleList == null) {
                articleViewModel.getArticles()
            }

            articleViewModel.articleList?.collectLatest { pagingData ->
                articlePagingAdapter.submitData(pagingData)
            }
        }
    }

    private fun observeResultState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                articleViewModel.articleState.collectLatest { resultState ->
                    when (resultState) {
                        is ResultState.Loading -> {
                            Log.d(TAG, "Loading...")
                        }
                        is ResultState.Success -> {
                            Log.d(TAG, resultState.message)
                        }
                        is ResultState.Error -> {
                            Log.d(TAG, resultState.message)
                        }
                    }
                }
            }
        }
    }

    private fun openArticleUrl(url: String) {
        val validUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "https://$url"
        } else {
            url
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(validUrl))
        try {
            requireContext().startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.unable_open_link),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ArticleFragment"
    }
}
