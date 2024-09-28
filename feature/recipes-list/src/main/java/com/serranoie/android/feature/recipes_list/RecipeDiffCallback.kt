package com.serranoie.android.feature.recipes_list

import androidx.recyclerview.widget.DiffUtil
import com.serranoie.android.core.domain.model.recipe.Recipe

class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {

    // Check if two Recipe items are the same based on their unique ID
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.id == newItem.id
    }

    // Check if the contents of two Recipe items are the same (e.g., title, description, image URL)
    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem // Assuming Recipe is a data class, this compares all properties
    }
}