package com.serranoie.android.feature.recipes_list.domain.usecase

import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult
import io.reactivex.rxjava3.core.Single

class GetRandomRecipesUseCase(private val repository: SpoonacularRepository) {
    operator fun invoke(): Single<DataResult<List<Recipe>>> {
        return repository.getRandomRecipes()
    }
}