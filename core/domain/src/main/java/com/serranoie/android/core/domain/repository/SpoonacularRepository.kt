package com.serranoie.android.core.domain.repository

import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.result.Result

interface SpoonacularRepository {
    suspend fun getRandomRecipes(): Result<List<Recipe>>
}