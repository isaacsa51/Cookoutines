package com.serranoie.android.feature.recipes_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.feature.recipes_list.R
import com.serranoie.android.feature.recipes_list.RecipesListViewModel
import com.serranoie.android.feature.recipes_list.databinding.ItemRecipeBinding

class RandomRecipeDelegate(private val viewModel: RecipesListViewModel) : RecipeAdapterDelegate {

    override fun getViewType(): Int = RecipeViewType.RANDOM.ordinal

    override fun isForViewType(item: RecipeListItem): Boolean = true

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RandomRecipeViewHolder(binding)
    }

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: RecipeListItem) {
        (holder as RandomRecipeViewHolder).bind((item as RecipeListItem.LatestRecipeItem).recipe)
    }

    inner class RandomRecipeViewHolder(private val binding: ItemRecipeBinding) :
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