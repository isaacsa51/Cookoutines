package com.serranoie.android.feature.saved

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.feature.saved.databinding.SavedRecipeItemBinding

class SavedRecipesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        items[position] as Recipe
    }

    fun submitList(data: MutableList<Recipe>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }


    inner class SavedRecipeViewHolder(private val binding: SavedRecipeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Recipe) {
            binding.recipeTitleTextView.text = data.title
            binding.recipeImageView.load(data.image) {
                crossfade(true)
                crossfade(500)
                placeholder(R.drawable.placeholder_image)
                error(R.drawable.placeholder_image)
            }
        }
    }
}