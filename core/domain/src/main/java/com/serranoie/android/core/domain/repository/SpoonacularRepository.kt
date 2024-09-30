package com.serranoie.android.core.domain.repository

import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.result.DataResult

interface SpoonacularRepository {
    suspend fun getRandomRecipes(): DataResult<List<Recipe>>
    suspend fun getTrendingRecipes(): DataResult<List<Recipe>>
}