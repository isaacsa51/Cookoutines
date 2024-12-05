package com.serranoie.android.feature.saved.domain.usecases

import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult

class GetSavedRecipesUseCase(private val repository: SpoonacularRepository) {
    suspend operator fun invoke(): DataResult<List<Recipe>> {
        return repository.getSavedRecipesByDate()
    }
}