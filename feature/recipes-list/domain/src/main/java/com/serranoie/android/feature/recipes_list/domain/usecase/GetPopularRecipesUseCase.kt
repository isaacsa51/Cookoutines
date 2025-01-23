package com.serranoie.android.feature.recipes_list.domain.usecase

import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult
import io.reactivex.rxjava3.core.Single

class GetPopularRecipesUseCase(private val repository: SpoonacularRepository) {
    operator fun invoke(): Single<DataResult<List<Result>>> {
        return repository.getPopularRecipes()
    }

}