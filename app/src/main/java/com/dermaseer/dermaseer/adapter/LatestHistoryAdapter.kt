package com.dermaseer.dermaseer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dermaseer.dermaseer.R
import com.dermaseer.dermaseer.data.remote.models.HistoryResponse
import com.dermaseer.dermaseer.databinding.ItemLatestHistoryBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class LatestHistoryAdapter: RecyclerView.Adapter<LatestHistoryAdapter.ViewHolder>() {

   private var historyData: List<HistoryResponse.Data?>? = listOf()
   private lateinit var onItemClickCallback: OnItemClickCallback

   interface OnItemClickCallback {
      fun onItemClicked(index: Int)
   }

   class ViewHolder(val binding: ItemLatestHistoryBinding): RecyclerView.ViewHolder(binding.root) {
      fun bind(data: HistoryResponse.Data?) {
         val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
         }
         val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
         val parsedDate = data?.createdAt?.let { inputFormat.parse(it) }
         val formattedDate = parsedDate?.let { outputFormat.format(it) } ?: "Unknown Date"
         with(binding) {
            Glide.with(imageView)
               .load(data?.imageUrl)
               .error(R.drawable.noimage)
               .into(imageView)
            tvDate.text = formattedDate
            acneType.text = data?.acneType
            chipSkinType.text = data?.result?.skinType
            chipProductType.text = data?.result?.productCategory
         }
      }
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val layoutInflater = LayoutInflater.from(parent.context)
      val binding = ItemLatestHistoryBinding.inflate(layoutInflater, parent, false)
      return ViewHolder(binding)
   }

   override fun getItemCount() = historyData?.size ?: 0

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.bind(historyData?.get(position))
      holder.itemView.setOnClickListener {
         onItemClickCallback.onItemClicked(position)
      }
   }

   @SuppressLint("NotifyDataSetChanged")
   fun setData(historyData: List<HistoryResponse.Data?>?) {
      this.historyData = historyData?.takeLast(1)
      notifyDataSetChanged()
   }

   fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
      this.onItemClickCallback = onItemClickCallback
   }
}