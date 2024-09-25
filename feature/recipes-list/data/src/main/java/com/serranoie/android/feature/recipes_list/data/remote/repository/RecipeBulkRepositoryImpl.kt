package com.serranoie.android.feature.recipes_list.data.remote.repository

import com.serranoie.android.core.data.mappers.toDomain
import com.serranoie.android.core.data.remote.SpoonacularApi
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.Result
import retrofit2.HttpException

class RecipeBulkRepositoryImpl(private val api: SpoonacularApi): SpoonacularRepository {

    override suspend fun getRecipes(): Result<List<Recipe>> {
        return try {
            val response = api.getRecipes().execute()

            when {
                response.isSuccessful -> {
                    val recipes = response.body()?.map { it.toDomain() }
                    Result.Success(recipes ?: emptyList())
                }
                response.code() == 400 -> {
                    Result.Error(HttpException(response))
                }
                else -> {
                    Result.Error(Exception("API Error: ${response.code()} ${response.message()}"))
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}