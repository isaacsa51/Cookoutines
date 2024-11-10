package com.serranoie.android.feature.recipes_list.domain.usecase

import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult

class SearchRecipeUseCase(private val repository: SpoonacularRepository) {
    suspend operator fun invoke(query: String): DataResult<List<Result>> {
        return repository.searchRecipes(query)
    }
}