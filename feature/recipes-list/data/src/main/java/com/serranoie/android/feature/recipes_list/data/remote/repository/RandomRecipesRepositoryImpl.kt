package com.serranoie.android.feature.recipes_list.data.remote.repository

import com.serranoie.android.core.data.mappers.toDomain
import com.serranoie.android.core.data.remote.SpoonacularApi
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult

class RandomRecipesRepositoryImpl(private val api: SpoonacularApi): SpoonacularRepository {

    override suspend fun getRandomRecipes(): DataResult<List<Recipe>> {
        return try {
            val response = api.getRecipes().execute()
            if (response.isSuccessful) {
                val recipes = response.body()?.recipes?.map { it.toDomain() }
                DataResult.Success(recipes ?: emptyList())
            } else {
                // Handle error cases
                DataResult.Error(Exception("API request failed"))
            }
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

}