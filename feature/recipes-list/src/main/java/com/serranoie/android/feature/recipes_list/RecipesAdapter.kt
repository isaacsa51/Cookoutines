package com.serranoie.android.feature.recipes_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.launch
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.feature.recipes_list.databinding.ItemRecipeBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipesAdapter @Inject constructor(
    private val viewModel: RecipesListViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Recipe>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? RecipeViewHolder)?.bind(items[position], viewModel)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(newItems: List<Recipe>) {
        val diffCallback = RecipeDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
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
                viewModel.viewModelScope.launch {
                    if (isSaved) {
                        viewModel.deleteRecipe(data.id!!)
                    } else {
                        viewModel.saveRecipe(data)
                    }
                    isSaved = !isSaved
                }
            }
        }
    }
}

class RecipeDiffCallback(private val oldList: List<Recipe>, private val newList: List<Recipe>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}