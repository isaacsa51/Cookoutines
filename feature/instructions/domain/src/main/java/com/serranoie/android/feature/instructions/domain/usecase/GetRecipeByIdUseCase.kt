package com.serranoie.android.feature.instructions.domain.usecase

import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult
import io.reactivex.rxjava3.core.Single

class GetRecipeByIdUseCase(private val recipeRepository: SpoonacularRepository){
    operator fun invoke(id: Int): Single<DataResult<Recipe>> {
        return recipeRepository.getRecipeById(id)
    }
}