package com.dermaseer.dermaseer.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.data.remote.models.ArticleResponse
import com.dermaseer.dermaseer.databinding.ItemArticleHomeBinding

class ArticleHomeAdapter :
    PagingDataAdapter<ArticleResponse.Data, ArticleHomeAdapter.ArticleHomeViewHolder>(
        ArticleDiffCallback()
    ) {

    class ArticleHomeViewHolder(private val binding: ItemArticleHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindArticle(article: ArticleResponse.Data) {
            Glide.with(binding.ivArticle.context)
                .load(article.imageUrl)
                .error(R.drawable.noimage)
                .into(binding.ivArticle)

            binding.ivArticle.setOnClickListener {
                val url = article.url
                if (url != null) {
                    val validUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        "https://$url"
                    } else {
                        url
                    }
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(validUrl))
                    try {
                        binding.root.context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(
                            binding.root.context,
                            "Tidak dapat membuka tautan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHomeViewHolder {
        val binding =
            ItemArticleHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleHomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleHomeViewHolder, position: Int) {
        val currentArticle = getItem(position)
        if (currentArticle != null) {
            holder.bindArticle(currentArticle)
        }
    }

    class ArticleDiffCallback : DiffUtil.ItemCallback<ArticleResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: ArticleResponse.Data,
            newItem: ArticleResponse.Data
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ArticleResponse.Data,
            newItem: ArticleResponse.Data
        ): Boolean {
            return oldItem == newItem
        }
    }
}
