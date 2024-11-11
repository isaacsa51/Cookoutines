package com.serranoie.android.core.data.remote.repository

import android.util.Log
import com.serranoie.android.core.data.mappers.toDomain
import com.serranoie.android.core.data.mappers.toListDomain
import com.serranoie.android.core.data.remote.SpoonacularApi
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.model.search.RecipeSearch
import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult

class RecipeRepositoryImpl(private val api: SpoonacularApi) : SpoonacularRepository {

    override suspend fun getRandomRecipes(): DataResult<List<Recipe>> {
        return try {
            val response = api.getRecipes().execute()
            if (response.isSuccessful) {
                val recipes = response.body()?.recipes?.map { it.toDomain() }
                DataResult.Success(recipes ?: emptyList())
            } else {
                DataResult.Error(Exception("API request failed"))
            }
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun getPopularRecipes(): DataResult<List<Result>> {
        return try {
            val response = api.getPopularRecipes().execute()

            if (response.isSuccessful) {
                val resultsDtoList = response.body()?.results.orEmpty()  // Safely get results or an empty list
                val recipes = resultsDtoList.mapNotNull { it?.toDomain() }  // Map each ResultDto to Result, ignoring nulls

                Log.d("POPULAR RESPONSE", recipes.toString())

                DataResult.Success(recipes)
            } else {
                Log.d("POPULAR ERROR", response.message().toString())
                DataResult.Error(Exception("API request failed"))
            }
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun getRecipeById(id: Int): DataResult<Recipe> {
        return try {
            val response = api.getRecipeDetails(id).execute()

            if (response.isSuccessful) {
                val recipe = response.body()

                DataResult.Success(recipe!!)
            } else {
                DataResult.Error(Exception("API request failed"))
            }
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun searchRecipes(query: String): DataResult<List<Result>> {
        return try {
            val response = api.searchRecipes(query).execute()

            if (response.isSuccessful) {
                Log.d("RESPONSE", response.message())

                val recipes = response.body()?.toListDomain()

                Log.d("REPOSITORY", recipes.toString())
                DataResult.Success(recipes ?: emptyList())
            } else {
                Log.d("REPOSITORY", response.message())
                DataResult.Error(Exception("API request failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }
}