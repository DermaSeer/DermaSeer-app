package com.dermaseer.dermaseer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.data.remote.models.ProductRecommendationResponse
import com.dermaseer.dermaseer.databinding.ItemProductRecommendationBinding
import java.text.NumberFormat
import java.util.Locale

class ProductRecommendationAdapter: RecyclerView.Adapter<ProductRecommendationAdapter.ViewHolder>() {

   private var productRecommendationList: List<ProductRecommendationResponse.Data?>? = listOf()
   private lateinit var onItemClickCallback: OnItemClickCallback

   interface OnItemClickCallback {
      fun onItemClicked(predictId: String)
   }

   class ViewHolder(val binding: ItemProductRecommendationBinding): RecyclerView.ViewHolder(binding.root) {
      @SuppressLint("SetTextI18n", "DefaultLocale")
      fun bind(product: ProductRecommendationResponse.Data?) {
         with(binding) {
            Glide.with(ivProduct)
               .load(product?.imageUrl)
               .error(R.drawable.noimage)
               .into(ivProduct)
            tvProductName.text = product?.name
            val formattedPrice = NumberFormat.getNumberInstance(Locale("id", "ID")).format(product?.price)
            tvProductPrice.text = "Rp$formattedPrice"
            tvProductRating.text = String.format("%.1f", product?.productRating)
         }
      }
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val layoutInflater = LayoutInflater.from(parent.context)
      val binding = ItemProductRecommendationBinding.inflate(layoutInflater, parent, false)
      return ViewHolder(binding)
   }

   override fun getItemCount() = productRecommendationList?.size ?: 0

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.bind(productRecommendationList?.get(position))
      holder.itemView.setOnClickListener {
         onItemClickCallback.onItemClicked(productRecommendationList?.get(position)?.id ?: "")
      }
   }

   @SuppressLint("NotifyDataSetChanged")
   fun setData(productRecommendationList: List<ProductRecommendationResponse.Data?>?) {
      this.productRecommendationList = productRecommendationList
      notifyDataSetChanged()
   }

   fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
      this.onItemClickCallback = onItemClickCallback
   }
}