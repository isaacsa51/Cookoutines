package com.serranoie.android.feature.instructions.domain.usecase

import com.serranoie.android.core.domain.model.instructions.Instructions
import com.serranoie.android.core.domain.model.instructions.InstructionsItem
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult

class GetDetailedInstructionsUseCase(private val repository: SpoonacularRepository) {
    suspend operator fun invoke(id: Int): DataResult<List<InstructionsItem>> {
        return repository.getRecipeInstructions(id)
    }
}