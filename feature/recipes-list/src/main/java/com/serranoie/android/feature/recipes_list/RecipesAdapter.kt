package com.serranoie.android.feature.recipes_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.feature.recipes_list.databinding.ItemReciptBinding

class RecipesAdapter : ListAdapter<Recipe, RecipesAdapter.RecipeViewHolder>(RecipeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemReciptBinding.inflate(
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

    class RecipeViewHolder(private val binding: ItemReciptBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            // Bind the data with the view (e.g., title, description, etc.)
            binding.recipeTitleTextView.text = recipe.title
            binding.recipeDescriptionTextView.text = recipe.summary

            // Load image if available
            // You can use libraries like Glide or Coil to load images

            // Coil example:
//            binding.recipeImageView.load(recipe.imageUrl) {
//                placeholder(R.drawable.placeholder_image) // Optional placeholder
//                error(R.drawable.error_image) // Optional error image
//            }

            // Optional: Handle click events
            binding.root.setOnClickListener {
                // Handle item click
                // You can trigger callbacks or navigation here
            }
        }
    }
}
