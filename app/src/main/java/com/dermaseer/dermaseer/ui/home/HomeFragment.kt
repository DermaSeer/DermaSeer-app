package com.dermaseer.dermaseer.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.databinding.FragmentHomeBinding
import com.dermaseer.dermaseer.databinding.ItemProductTypeBinding

class HomeFragment : Fragment() {

   private var _binding: FragmentHomeBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController

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