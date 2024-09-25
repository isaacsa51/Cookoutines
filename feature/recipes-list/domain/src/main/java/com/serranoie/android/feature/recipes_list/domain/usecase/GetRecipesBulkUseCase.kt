package com.serranoie.android.feature.recipes_list.domain.usecase

import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.Result

class GetRecipesBulkUseCase(private val repository: SpoonacularRepository) {
    suspend operator fun invoke(): Result<List<Recipe>> {
        return repository.getRecipes()
    }
}