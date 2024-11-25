package com.dermaseer.dermaseer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.data.remote.models.ProductResponse
import com.dermaseer.dermaseer.databinding.ItemProductListBinding
import com.dermaseer.dermaseer.ui.product_list.ProductListFragmentDirections
import com.google.gson.Gson
import java.text.NumberFormat
import java.util.Locale

class ProductListAdapter :
    ListAdapter<ProductResponse.Data, ProductListAdapter.ProductViewHolder>(ProductDiffCallback()) {

    class ProductViewHolder(private val binding: ItemProductListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductResponse.Data) {
            binding.productName.text = product.name
            val formattedPrice = product.price?.let {
                NumberFormat.getCurrencyInstance(Locale.getDefault()).format(it)
            } ?: "Price not available"
            binding.productPrice.text = formattedPrice

            Glide.with(binding.root.context)
                .load(product.imageUrl)
                .placeholder(R.drawable.noimage)
                .into(binding.ivProduct)

            binding.root.setOnClickListener {
                val productJson = Gson().toJson(product)
                val action = ProductListFragmentDirections
                    .actionProductListFragmentToProductDetailFragment(productJson)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val binding =
            ItemProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<ProductResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: ProductResponse.Data,
            newItem: ProductResponse.Data
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductResponse.Data,
            newItem: ProductResponse.Data
        ): Boolean {
            return oldItem == newItem
        }
    }
}
