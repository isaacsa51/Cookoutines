package com.serranoie.android.core.domain.usecase

import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.repository.SpoonacularRepository

class SaveRecipeUseCase(private val repository: SpoonacularRepository) {
    suspend operator fun invoke(recipe: Recipe) {
        return repository.insertRecipe(recipe)
    }
}