package com.serranoie.android.feature.recipes_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.feature.recipes_list.databinding.ItemRecipeBinding

class RecipesAdapter : ListAdapter<Recipe, RecipesAdapter.RecipeViewHolder>(RecipeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }

    class RecipeViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            binding.recipeTitleTextView.text = recipe.title
            binding.authorTextView.text = recipe.creditsText

            binding.recipeImageView.load(recipe.image) {
                crossfade(true)
                placeholder(R.drawable.placeholder_image) // Optional placeholder
                error(R.drawable.placeholder_image) // Optional error image
            }

            // Optional: Handle click events
            binding.root.setOnClickListener {
                // Handle item click
                // You can trigger callbacks or navigation here
            }
        }
    }
}
