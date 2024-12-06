package com.serranoie.android.core.domain.usecase

import com.serranoie.android.core.domain.repository.SpoonacularRepository

class DeleteRecipeUseCase(private val repository: SpoonacularRepository) {
    suspend operator fun invoke(id: Int){
        repository.deleteRecipe(id)
    }
}