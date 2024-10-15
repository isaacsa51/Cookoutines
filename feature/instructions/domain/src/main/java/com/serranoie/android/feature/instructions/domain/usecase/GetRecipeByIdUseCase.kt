package com.serranoie.android.feature.instructions.domain.usecase

import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult

class GetRecipeByIdUseCase(private val recipeRepository: SpoonacularRepository){
    suspend operator fun invoke(id: Int): DataResult<Recipe> {
        return recipeRepository.getRecipeById(id)
    }
}