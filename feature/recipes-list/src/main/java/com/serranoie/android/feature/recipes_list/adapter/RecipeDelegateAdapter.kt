package com.serranoie.android.feature.recipes_list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class RecipeDelegateAdapter(
    val delegates: List<RecipeAdapterDelegate>
) : ListAdapter<RecipeListItem, RecyclerView.ViewHolder>(RecipeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates.find { it.getViewType() == viewType }?.createViewHolder(parent)
            ?: throw IllegalArgumentException("No delegate found for view type: $viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        delegates.find { it.getViewType() == holder.itemViewType }?.bindViewHolder(holder, item)
            ?: throw IllegalArgumentException("No delegate found for view type: ${holder.itemViewType}")
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return delegates.find { it.isForViewType(item) }?.getViewType()
            ?: throw IllegalArgumentException("No delegate found for item: $item")
    }
}

class RecipeDiffCallback : DiffUtil.ItemCallback<RecipeListItem>() {
    override fun areItemsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean {
        return when {
            oldItem is RecipeListItem.TrendingRecipeItem && newItem is RecipeListItem.TrendingRecipeItem ->
                oldItem.recipe.id == newItem.recipe.id
            oldItem is RecipeListItem.LatestRecipeItem && newItem is RecipeListItem.LatestRecipeItem ->
                oldItem.recipe.id == newItem.recipe.id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean {
        return oldItem == newItem
    }
}