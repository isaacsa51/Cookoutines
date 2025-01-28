package com.serranoie.android.feature.recipes_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.feature.recipes_list.R
import com.serranoie.android.feature.recipes_list.databinding.ItemPopularRecipeBinding

class TrendingRecipeDelegate : RecipeAdapterDelegate {

    override fun getViewType(): Int = RecipeViewType.TRENDING.ordinal

    override fun isForViewType(item: RecipeListItem): Boolean =
        item is RecipeListItem.TrendingRecipeItem

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding =
            ItemPopularRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrendingRecipeViewHolder(binding)
    }

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: RecipeListItem) {
        (holder as TrendingRecipeViewHolder).bind((item as RecipeListItem.TrendingRecipeItem).recipe)
    }

    class TrendingRecipeViewHolder(
        private val binding: ItemPopularRecipeBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Result) {
            binding.trendingTitleRecipe.text = recipe.title
            binding.authorTextView.text = recipe.title

            binding.imageTrendingRecipe.load(recipe.image) {
                crossfade(true)
                crossfade(500)
                placeholder(R.drawable.placeholder_image)
                error(R.drawable.placeholder_image)
            }

            binding.root.setOnClickListener {
                val recipeId = recipe.id
                val request = NavDeepLinkRequest.Builder
                    .fromUri("cookoutines://instructions/${recipeId?.toString()}".toUri())
                    .build()
                Navigation.findNavController(this.itemView).navigate(request)
            }
        }
    }
}
