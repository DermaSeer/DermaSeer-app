package com.dermaseer.dermaseer.ui.history_detail

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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.adapter.LatestHistoryAdapter
import com.dermaseer.dermaseer.databinding.FragmentHistoryDetailBinding
import com.dermaseer.dermaseer.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryDetailFragment : Fragment() {

   private var _binding: FragmentHistoryDetailBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController
   private val historyDetailViewModel: HistoryDetailViewModel by viewModels()
   private val args: HistoryDetailFragmentArgs by navArgs()

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      // Inflate the layout for this fragment
      _binding = FragmentHistoryDetailBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      navController = Navigation.findNavController(view)
      binding.topAppBar.setNavigationOnClickListener { navController.navigateUp() }
//      getHistoryDetail()
   }

   private fun getHistoryDetail() {
      historyDetailViewModel.getHistoryByIndex(args.index)
      historyDetailViewModel.state.observe(viewLifecycleOwner) { state ->
         when (state) {
            is ResultState.Loading -> {
               with(binding) {
                  scrollView.visibility = View.GONE
                  lottieLoading.visibility = View.VISIBLE
               }
            }
            is ResultState.Success -> {
               with(binding) {
                  historyDetailViewModel.historyData.observe(viewLifecycleOwner) { response ->
                     Glide.with(ivHistory)
                        .load(response.imageUrl)
                        .error(R.drawable.noimage)
                        .into(ivHistory)
                     tvAcneType.text = response.acneType
                     chipSkinType.text = response.result?.skinType
                     chipProductType.text = response.result?.productCategory
                     tvResultRecommendation.text = response.result?.msgRecommendation
                  }
                  lottieLoading.visibility = View.GONE
                  scrollView.visibility = View.VISIBLE
               }
            }
            is ResultState.Error -> {
               with(binding) {
                  lottieLoading.visibility = View.GONE
                  scrollView.visibility = View.VISIBLE
                  showStateDialog(R.drawable.remove, "Failed to get data")
               }
            }
         }
      }
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