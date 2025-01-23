package com.serranoie.android.feature.recipes_list.domain.usecase

import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult
import io.reactivex.rxjava3.core.Single

class SearchRecipeUseCase(private val repository: SpoonacularRepository) {
    operator fun invoke(query: String): Single<DataResult<List<Result>>> {
        return repository.searchRecipes(query)
    }
}