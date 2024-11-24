package com.dermaseer.dermaseer.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.data.remote.models.ArticleResponse
import com.dermaseer.dermaseer.databinding.ItemArticleHomeBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ArticleHomeAdapter :
    ListAdapter<ArticleResponse.Data, ArticleHomeAdapter.ArticleHomeViewHolder>(ArticleDiffCallback()) {

    class ArticleHomeViewHolder(private val binding: ItemArticleHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindArticle(article: ArticleResponse.Data) {
            Glide.with(binding.ivArticle.context)
                .load(article.imageUrl)
                .into(binding.ivArticle)

            binding.ivArticle.setOnClickListener {
                article.url?.let { url ->
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
        val sortedArticles = currentList.sortedByDescending { article ->
            val publishDateStr = article.publishDate?.toString()
            try {
                if (!publishDateStr.isNullOrEmpty()) {
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(
                        publishDateStr
                    )
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }.take(3)

        if (position < sortedArticles.size) {
            holder.bindArticle(sortedArticles[position])
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
