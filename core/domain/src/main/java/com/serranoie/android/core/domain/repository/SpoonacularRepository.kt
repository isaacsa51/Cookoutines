package com.serranoie.android.core.domain.repository

import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.model.search.RecipeSearch
import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.core.domain.result.DataResult

interface SpoonacularRepository {
    suspend fun getRandomRecipes(): DataResult<List<Recipe>>
    suspend fun getPopularRecipes(): DataResult<List<Result>>
    suspend fun getRecipeById(id: Int): DataResult<Recipe>
    suspend fun searchRecipes(query: String): DataResult<List<Result>>
}