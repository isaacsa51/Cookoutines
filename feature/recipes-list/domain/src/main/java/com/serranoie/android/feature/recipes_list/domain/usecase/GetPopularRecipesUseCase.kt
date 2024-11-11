package com.serranoie.android.feature.recipes_list.domain.usecase

import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult

class GetPopularRecipesUseCase(private val repository: SpoonacularRepository) {
    suspend operator fun invoke(): DataResult<List<Result>> {
        return repository.getPopularRecipes()
    }

}