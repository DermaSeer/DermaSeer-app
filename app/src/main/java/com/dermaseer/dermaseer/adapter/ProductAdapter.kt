package com.dermaseer.dermaseer.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.data.remote.models.ProductResponse
import com.dermaseer.dermaseer.databinding.ItemProductListBinding
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val products: List<ProductResponse.Data?>,
    private val onItemClick: (ProductResponse.Data) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(private val binding: ItemProductListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: ProductResponse.Data?) {
            product?.let {
                binding.productName.text = product.name
                val formattedPrice = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(product.price)
                binding.productPrice.text = formattedPrice
                Glide.with(binding.root.context)
                    .load(product.imageUrl)
                    .placeholder(R.drawable.noimage)
                    .into(binding.ivProduct)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size
}
