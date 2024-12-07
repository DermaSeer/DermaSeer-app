package com.dermaseer.dermaseer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dermaseer.dermaseer.databinding.ItemMatchedIngredientsBinding

class MatchedIngredientsAdapter(private val ingredients: List<String>) : RecyclerView.Adapter<MatchedIngredientsAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = ItemMatchedIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.bind(ingredient)
    }

    override fun getItemCount(): Int = ingredients.size

    inner class IngredientViewHolder(private val binding: ItemMatchedIngredientsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: String) {
            binding.chipMatchedIngredients.text = ingredient
        }
    }
}

