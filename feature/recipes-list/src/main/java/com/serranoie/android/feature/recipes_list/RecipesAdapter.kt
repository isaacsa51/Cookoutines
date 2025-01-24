package com.serranoie.android.feature.recipes_list

import android.view.LayoutInflater
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
import javax.inject.Inject

class RecipesAdapter @Inject constructor(
    private val viewModel: RecipesListViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecipeViewHolder -> {
                val recipe = items[position] as Recipe
                holder.bind(recipe, viewModel)
            }

            is RecipeSearchViewHolder -> {
                val recipeSearch = items[position] as Result
                holder.bind(recipeSearch)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(newItems: List<Recipe>) {
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

        private var isSaved = false

        fun bind(data: Recipe, viewModel: RecipesListViewModel) {
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

            binding.saveButton.setOnClickListener {
                isSaved = !isSaved
                if (isSaved) {
                    viewModel.saveRecipe(data)
                    binding.saveButton.setIconResource(R.drawable.ic_bookmarked)
                } else {
                    viewModel.deleteRecipe(data.id!!)
                    binding.saveButton.setIconResource(R.drawable.ic_bookmark)
                }
            }
        }
    }
}
