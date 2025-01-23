package com.serranoie.android.feature.instructions.domain.usecase

import com.serranoie.android.core.domain.model.instructions.InstructionsItem
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult
import io.reactivex.rxjava3.core.Single

class GetDetailedInstructionsUseCase(private val repository: SpoonacularRepository) {
    operator fun invoke(id: Int): Single<DataResult<List<InstructionsItem>>> {
        return repository.getRecipeInstructions(id)
    }
}