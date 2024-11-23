package com.dermaseer.dermaseer.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.data.remote.models.ArticleResponse
import com.dermaseer.dermaseer.databinding.ItemArticleBinding

class ArticleAdapter(
    private var articles: List<ArticleResponse.Data>,
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

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
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newArticles: List<ArticleResponse.Data>) {
        articles = newArticles
        notifyDataSetChanged()
    }
}
