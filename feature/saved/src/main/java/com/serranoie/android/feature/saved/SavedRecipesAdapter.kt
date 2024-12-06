package com.serranoie.android.feature.saved

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.feature.saved.databinding.SavedRecipeItemBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class SavedRecipesAdapter @Inject constructor(
    private val viewModel: SavedRecipesViewModel,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedRecipeViewHolder {
        val binding =
            SavedRecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedRecipeViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SavedRecipeViewHolder -> {
                val recipe = items[position] as Recipe
                holder.bind(recipe, viewModel, position, lifecycleScope)
            }
        }
    }

    fun submitList(data: List<Recipe>) {
        items.addAll(data)
        notifyDataSetChanged()
    }

    inner class SavedRecipeViewHolder(private val binding: SavedRecipeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Recipe, viewModel: SavedRecipesViewModel, position: Int, lifecycleScope: LifecycleCoroutineScope) {
            binding.recipeTitleTextView.text = data.title
            binding.recipeImageView.load(data.image) {
                crossfade(true)
                crossfade(500)
                placeholder(R.drawable.placeholder_image)
                error(R.drawable.placeholder_image)
            }

            binding.deleteButton.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.deleteRecipe(data.id!!)
                }
                items.removeAt(position) // Remove item from the items list
                notifyItemRemoved(position) // Notify adapter of data change
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
}