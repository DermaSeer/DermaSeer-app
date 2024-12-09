package com.dermaseer.dermaseer.ui.home

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.adapter.ArticleHomeAdapter
import com.dermaseer.dermaseer.adapter.LatestHistoryAdapter
import com.dermaseer.dermaseer.data.remote.models.ArticleResponse
import com.dermaseer.dermaseer.databinding.FragmentHomeBinding
import com.dermaseer.dermaseer.databinding.ItemProductTypeBinding
import com.dermaseer.dermaseer.ui.history.HistoryFragmentDirections
import com.dermaseer.dermaseer.utils.ResultState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var articleHomeAdapter: ArticleHomeAdapter
    private val latestHistoryAdapter: LatestHistoryAdapter = LatestHistoryAdapter()
    private val auth = FirebaseAuth.getInstance()

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
        getUserData()
        getArticles()
        getLatestHistory()
        getHistoryDetail()
        homeState()
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
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(
                            publishDateStr
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
            }.take(3)

        val pagingData = PagingData.from(sortedArticles)
        viewLifecycleOwner.lifecycleScope.launch {
            articleHomeAdapter.submitData(pagingData)
        }
    }

    private fun setProductType() {
        val listProductType = listOf(
            ProductType(R.drawable.moisturizer, requireContext().getString(R.string.Pelembab)),
            ProductType(R.drawable.toner, requireContext().getString(R.string.toner)),
            ProductType(R.drawable.serum, requireContext().getString(R.string.essence)),
            ProductType(R.drawable.facewash, requireContext().getString(R.string.pembersih)),
            ProductType(R.drawable.sunscreen, requireContext().getString(R.string.Sunscreen)),
        )

        val listCard: List<ItemProductTypeBinding>
        with(binding) {
            listCard = listOf(cardMoisturizer, cardToner, cardSerum, cardFacewash, cardSunscreen)
        }

        for (i in listProductType.indices) {
            listCard[i].tvProductType.text = listProductType[i].productTitle
            listCard[i].tvProductImage.setImageResource(listProductType[i].productImage)

            listCard[i].root.setOnClickListener {
                val selectedCategory = listProductType[i].productTitle
                val action =
                    HomeFragmentDirections.actionHomeFragmentToProductListFragment(selectedCategory)
                navController.navigate(action)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.userData.observe(viewLifecycleOwner) { user ->
                with(binding) {
                    Glide.with(ivUserPhoto)
                        .load(auth.currentUser?.photoUrl)
                        .error(R.drawable.unknownperson)
                        .into(ivUserPhoto)
                    tvHelloUser.text = "Hello, ${user.data?.user?.name}!"
                }
            }
        }
    }

    private fun getArticles() {
        homeViewModel.articles.observe(viewLifecycleOwner) { pagingData ->
            articleHomeAdapter.submitData(lifecycle, pagingData)
        }
    }

    private fun getLatestHistory() {
        homeViewModel.latestHistory.observe(viewLifecycleOwner) { response ->
            if (response.data?.isEmpty() == true) {
                binding.tvNoLatestHistory.visibility = View.VISIBLE
            } else {
                binding.rvLatestHistory.apply {
                    adapter = latestHistoryAdapter
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    latestHistoryAdapter.setData(response.data)
                }
            }
        }
    }

    private fun homeState() {
        homeViewModel.homeState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    with(binding) {
                        ivLogo.visibility = View.GONE
                        ivUserPhoto.visibility = View.GONE
                        tvHelloUser.visibility = View.GONE
                        tvArticle.visibility = View.GONE
                        tvProduct.visibility = View.GONE
                        tvLatestHistory.visibility = View.GONE
                        containerProduct.visibility = View.GONE
                        lottieLoading.visibility = View.VISIBLE
                        lottieLoading.playAnimation()
                    }
                }
                is ResultState.Success -> {
                    with(binding) {
                        ivLogo.visibility = View.VISIBLE
                        ivUserPhoto.visibility = View.VISIBLE
                        tvHelloUser.visibility = View.VISIBLE
                        tvArticle.visibility = View.VISIBLE
                        tvProduct.visibility = View.VISIBLE
                        tvLatestHistory.visibility = View.VISIBLE
                        containerProduct.visibility = View.VISIBLE
                        lottieLoading.visibility = View.GONE
                        lottieLoading.cancelAnimation()
                    }
                }
                is ResultState.Error -> {
                    binding.lottieLoading.visibility = View.GONE
                    binding.lottieLoading.cancelAnimation()
                    showStateDialog(R.drawable.remove, result.errorMessage)
                }
            }
        }
    }

    private fun getHistoryDetail() {
        latestHistoryAdapter.setOnItemClickCallback(object: LatestHistoryAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String) {
                val toDetail = HomeFragmentDirections.actionHomeFragmentToHistoryDetailFragment(id)
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

        val dialog = builder.setView(customView).create()
        btnDismiss.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class ProductType(
    val productImage: Int,
    val productTitle: String
)