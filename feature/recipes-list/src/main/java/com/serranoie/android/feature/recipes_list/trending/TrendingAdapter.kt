package com.serranoie.android.feature.recipes_list.trending

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.feature.recipes_list.R
import com.serranoie.android.feature.recipes_list.databinding.ItemPopularRecipeBinding

class TrendingAdapter : ListAdapter<Recipe, TrendingAdapter.TrendingViewHolder>(TrendingDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrendingViewHolder {
        val binding = ItemPopularRecipeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TrendingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrendingAdapter.TrendingViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }

    class TrendingViewHolder(private val binding: ItemPopularRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {

            binding.trendingTitleRecipe.text = recipe.title

            binding.imageTrendingRecipe.load(recipe.image) {
                crossfade(true)
                crossfade(500)
                placeholder(R.drawable.placeholder_image)
                error(R.drawable.placeholder_image)
            }
        }
    }
}