package com.serranoie.android.core.domain.repository

import com.serranoie.android.core.domain.model.instructions.InstructionsItem
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.core.domain.result.DataResult
import io.reactivex.rxjava3.core.Single

interface SpoonacularRepository {
    fun getRandomRecipes(): Single<DataResult<List<Recipe>>>
    fun getPopularRecipes(): Single<DataResult<List<Result>>>
    fun getRecipeById(id: Int): Single<DataResult<Recipe>>
    fun searchRecipes(query: String): Single<DataResult<List<Result>>>
    fun getRecipeInstructions(id: Int): Single<DataResult<List<InstructionsItem>>>

    // Local functions
    fun insertRecipe(recipe: Recipe)
    fun deleteRecipe(id: Int)
    fun getSavedRecipesByDate(): Single<DataResult<List<Recipe>>>
}