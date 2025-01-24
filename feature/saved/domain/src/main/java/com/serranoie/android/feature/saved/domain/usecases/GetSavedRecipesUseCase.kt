package com.serranoie.android.feature.saved.domain.usecases

import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult
import io.reactivex.rxjava3.core.Single

class GetSavedRecipesUseCase(private val repository: SpoonacularRepository) {
    operator fun invoke(): Single<DataResult<List<Recipe>>> {
        return repository.getSavedRecipesByDate()
    }
}