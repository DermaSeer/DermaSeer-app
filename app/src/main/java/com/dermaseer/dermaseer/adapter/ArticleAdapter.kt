package com.dermaseer.dermaseer.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.data.remote.models.ArticleResponse
import com.dermaseer.dermaseer.databinding.ItemArticleBinding

class ArticleAdapter :
    ListAdapter<ArticleResponse.Data, ArticleAdapter.ArticleViewHolder>(ArticleDiffCallback()) {

    class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticleResponse.Data) {
            binding.tvArticleTitle.text = article.title ?: "Untitled"
            binding.tvArticleDescription.text = article.content ?: "No description available"
            Glide.with(binding.ivArticleImage.context)
                .load(article.imageUrl)
                .placeholder(com.dermaseer.dermaseer.R.drawable.noimage)
                .into(binding.ivArticleImage)

            binding.icArrow.setOnClickListener {
                article.url?.let { url ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleViewHolder {
        val binding =
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
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
