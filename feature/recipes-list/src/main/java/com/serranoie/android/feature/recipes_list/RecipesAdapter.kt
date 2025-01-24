package com.serranoie.android.feature.recipes_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.feature.recipes_list.databinding.ItemRecipeBinding
import javax.inject.Inject

class RecipesAdapter(private val viewModel: RecipesListViewModel) :
    ListAdapter<Recipe, RecipesAdapter.RecipeViewHolder>(RecipeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }

    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isSaved = false
        private lateinit var currentRecipe: Recipe

        init {
            binding.saveButton.setOnClickListener {
                isSaved = !isSaved
                if (isSaved) {
                    saveRecipe(currentRecipe)
                    binding.saveButton.setIconResource(R.drawable.ic_bookmarked)
                    Snackbar.make(binding.root, "Recipe saved!", Snackbar.LENGTH_SHORT).show()
                } else {
                    deleteRecipe(currentRecipe.id!!)
                    binding.saveButton.setIconResource(R.drawable.ic_bookmark)
                    Snackbar.make(binding.root, "Recipe deleted", Snackbar.LENGTH_SHORT).show()

                }
            }
        }

        fun bind(data: Recipe) {
            currentRecipe = data
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

    private fun saveRecipe(recipe: Recipe) {
        viewModel.saveRecipe(recipe)
    }

    private fun deleteRecipe(recipeId: Int) {
        viewModel.deleteRecipe(recipeId)
    }
}

class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }
}