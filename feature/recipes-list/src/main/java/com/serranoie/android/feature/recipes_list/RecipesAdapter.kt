package com.serranoie.android.feature.recipes_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.feature.recipes_list.databinding.ItemRecipeBinding

class RecipesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Any>()
    private var isSearchAdapter = false


    override fun getItemViewType(position: Int): Int {
        return if (isSearchAdapter && items.isEmpty()) {
            TYPE_EMPTY_SEARCH // New view type for empty search
        } else {
            when (items[position]) {
                is Recipe -> TYPE_RECIPE
                is Result -> TYPE_RECIPE_SEARCH
                else -> throw IllegalArgumentException("Unknown type")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_RECIPE -> {
                val binding =
                    ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RecipeViewHolder(binding)
            }

            TYPE_RECIPE_SEARCH -> {
                val binding =
                    ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RecipeSearchViewHolder(binding)
            }

//            TYPE_EMPTY_SEARCH -> {
//                val view = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.item_empty_search, parent, false) // Create empty view layout
//                EmptySearchViewHolder(view)
//            }

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecipeViewHolder -> {
                val recipe = items[position] as Recipe
                holder.bind(recipe)
            }

            is RecipeSearchViewHolder -> {
                val recipeSearch = items[position] as Result
                holder.bind(recipeSearch)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (isSearchAdapter && items.isEmpty()) {
            1
        } else {
            items.size
        }
    }

    fun submitList(newItems: List<Any>, isSearch: Boolean = false) {
        isSearchAdapter = isSearch
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class RecipeSearchViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Result) {
            binding.recipeTitleTextView.text = data.title
            binding.authorTextView.isVisible = false
            binding.labelCredits.isVisible = false

            binding.recipeImageView.load(data.image) {
                crossfade(true)
                crossfade(500)
                placeholder(R.drawable.placeholder_image)
                error(R.drawable.placeholder_image)
            }

            binding.root.setOnClickListener {
                val recipeId = data.id
                val request = NavDeepLinkRequest.Builder
                    .fromUri("cookoutines://instructions/${recipeId?.toString()}".toUri())
                    .build()
                findNavController(this.itemView).navigate(request)
            }
        }
    }

    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Recipe) {
            binding.recipeTitleTextView.text = data.title
            binding.authorTextView.text = data.creditsText

            binding.recipeImageView.load(data.image) {
                crossfade(true)
                crossfade(500)
                placeholder(R.drawable.placeholder_image)
                error(R.drawable.placeholder_image)
            }

            binding.root.setOnClickListener {
                val recipeId = data.id
                val request = NavDeepLinkRequest.Builder
                    .fromUri("cookoutines://instructions/${recipeId?.toString()}".toUri())
                    .build()
                findNavController(this.itemView).navigate(request)
            }
        }
    }

    inner class EmptySearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // You can add any specific binding logic for the empty view here
    }

    companion object {
        private const val TYPE_RECIPE = 0
        private const val TYPE_RECIPE_SEARCH = 1
        private const val TYPE_EMPTY_SEARCH = 2
    }
}
