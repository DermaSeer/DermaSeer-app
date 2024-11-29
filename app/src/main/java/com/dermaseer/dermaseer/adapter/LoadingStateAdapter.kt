package com.dermaseer.dermaseer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dermaseer.dermaseer.databinding.LoadingItemBinding

class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val binding = LoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(
        holder: LoadingStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    class LoadingStateViewHolder(private val binding: LoadingItemBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                tvErrorMessage.isVisible = loadState is LoadState.Error

                if (loadState is LoadState.Error) {
                    tvErrorMessage.text = loadState.error.localizedMessage
                }
            }
        }
    }
}