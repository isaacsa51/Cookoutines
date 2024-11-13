package com.serranoie.android.feature.instructions.ingredients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.serranoie.android.core.domain.model.recipe.ExtendedIngredient
import com.serranoie.android.feature.instructions.R
import com.serranoie.android.feature.instructions.databinding.IngredientItemBinding
import com.serranoie.android.feature.instructions.utils.UiConstants
import java.util.Locale

class IngredientsAdapter(
    private val ingredients: List<ExtendedIngredient>
) : RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = IngredientItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    override fun getItemCount(): Int = ingredients.size

    inner class IngredientViewHolder(private val binding: IngredientItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: ExtendedIngredient) {

            binding.ingredientName.text = ingredient.name?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }

            binding.ingredientAmount.text = "${ingredient.amount} ${ingredient.unit}"

            binding.imageIngredient.load(UiConstants.IMAGE_BASE_URL + ingredient.image) {
                crossfade(true)
                crossfade(500)
                placeholder(R.drawable.placeholder_image)
                error(R.drawable.placeholder_image)
            }
        }
    }
}