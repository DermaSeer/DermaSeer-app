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
import com.dermaseer.dermaseer.databinding.ItemArticleBinding

class ArticleAdapter(private val onItemClick: (String) -> Unit) :
    ListAdapter<ArticleResponse.Data, ArticleAdapter.ArticleViewHolder>(ArticleDiffCallback()) {

    class ArticleViewHolder(private val binding: ItemArticleBinding, private val onItemClick: (String) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticleResponse.Data) {
            with(binding) {
                tvArticleTitle.text = article.title ?: "Untitled"
                Glide.with(ivArticleImage.context)
                    .load(article.imageUrl)
                    .into(ivArticleImage)

                root.setOnClickListener {
                    article.url?.let(onItemClick)
                }

                icArrow.setOnClickListener {
                    article.url?.let { url ->
                        val validUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            "https://$url"
                        } else {
                            url
                        }

                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(validUrl))
                        try {
                            root.context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(
                                root.context,
                                "Tidak dapat membuka tautan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
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
        return ArticleViewHolder(binding, onItemClick)
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
