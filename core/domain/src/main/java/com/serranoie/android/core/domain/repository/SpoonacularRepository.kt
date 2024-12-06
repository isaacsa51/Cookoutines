package com.serranoie.android.core.domain.repository

import com.serranoie.android.core.domain.model.instructions.InstructionsItem
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.core.domain.result.DataResult

interface SpoonacularRepository {
    suspend fun getRandomRecipes(): DataResult<List<Recipe>>
    suspend fun getPopularRecipes(): DataResult<List<Result>>
    suspend fun getRecipeById(id: Int): DataResult<Recipe>
    suspend fun searchRecipes(query: String): DataResult<List<Result>>
    suspend fun getRecipeInstructions(id: Int): DataResult<List<InstructionsItem>>

    // Local functions
    suspend fun insertRecipe(recipe: Recipe)
    suspend fun deleteRecipe(id: Int)
    suspend fun getSavedRecipesByDate(): DataResult<List<Recipe>>
}