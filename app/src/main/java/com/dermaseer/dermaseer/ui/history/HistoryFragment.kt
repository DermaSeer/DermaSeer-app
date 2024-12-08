package com.dermaseer.dermaseer.ui.history

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.adapter.AllHistoryAdapter
import com.dermaseer.dermaseer.databinding.FragmentEditProfileBinding
import com.dermaseer.dermaseer.databinding.FragmentHistoryBinding
import com.dermaseer.dermaseer.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

   private var _binding: FragmentHistoryBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController
   private val historyViewModel: HistoryViewModel by viewModels()
   private val historyAdapter: AllHistoryAdapter = AllHistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
       _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      navController = Navigation.findNavController(view)
//      getAllHistory()
      getHistoryDetail()
   }

   private fun getAllHistory() {
      historyViewModel.state.observe(viewLifecycleOwner) { state ->
         when (state) {
            is ResultState.Loading -> {
               binding.lottieLoading.visibility = View.VISIBLE
            }
            is ResultState.Success -> {
               with(binding) {
                  lottieLoading.visibility = View.GONE
                  historyViewModel.historyData.observe(viewLifecycleOwner) { response ->
                     if (response.data?.isEmpty() == true) {
                        ivNoData.visibility = View.VISIBLE
                     } else {
                        rvHistory.apply {
                           adapter = historyAdapter
                           setHasFixedSize(true)
                           layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                           historyAdapter.setData(response?.data)
                        }
                     }
                  }
               }
            }
            is ResultState.Error -> {
               binding.lottieLoading.visibility = View.GONE
               showStateDialog(R.drawable.remove, "Failed to get data")
            }
         }
      }
   }

   private fun getHistoryDetail() {
      historyAdapter.setOnItemClickCallback(object: AllHistoryAdapter.OnItemClickCallback {
         override fun onItemClicked(index: Int) {
            val toDetail = HistoryFragmentDirections.actionHistoryFragmentToHistoryDetailFragment(index)
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