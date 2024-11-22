package com.serranoie.android.core.data.remote.repository

import android.util.Log
import com.serranoie.android.core.data.local.dao.RecipesDao
import com.serranoie.android.core.data.local.entity.RecipeEntity
import com.serranoie.android.core.data.mappers.toDomain
import com.serranoie.android.core.data.mappers.toListDomain
import com.serranoie.android.core.data.remote.SpoonacularApi
import com.serranoie.android.core.domain.model.instructions.InstructionsItem
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RecipeRepositoryImpl @Inject constructor(
    private val api: SpoonacularApi,
    private val recipesDao: RecipesDao,
    private val ioDispatcher: CoroutineDispatcher // Inject ioDispatcher
) : SpoonacularRepository {
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

    override suspend fun getRecipeById(id: Int): DataResult<Recipe> {
        return try {
            val response = api.getRecipeDetails(id).execute()

            if (response.isSuccessful) {
                val recipe = response.body()?.toDomain()

                DataResult.Success(recipe!!)
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
                val resultsDtoList = response.body()?.results.orEmpty()
                val recipes = resultsDtoList.mapNotNull { it?.toDomain() }

                DataResult.Success(recipes)
            } else {
                Log.d("POPULAR ERROR", response.message().toString())
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
                val resultsDtoList = response.body()?.results.orEmpty()
                val recipes = resultsDtoList.mapNotNull { it?.toDomain() }

                DataResult.Success(recipes)
            } else {
                DataResult.Error(Exception("API request failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun getRecipeInstructions(id: Int): DataResult<List<InstructionsItem>> {
        return try {
            val response = api.getRecipeInstructions(id).execute()

            if (response.isSuccessful) {
                val responseBody = response.body()
                try {
                    val mappedData = responseBody?.toListDomain()
                    DataResult.Success(mappedData!!)
                } catch (e: Exception) {
                    Log.d("REPOSITORY", "EXCEPTION: ${e.message}")
                    DataResult.Error(e)
                }
            } else {
                Log.d("REPOSITORY", "EXCEPTION: ${response.message()}")
                DataResult.Error(Exception("API request failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun insertRecipe(recipe: Recipe) {
        withContext(ioDispatcher) {
            recipesDao.insertRecipe(recipe)
        }
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        withContext(ioDispatcher) {
            recipesDao.deleteRecipe(recipe)
        }
    }

    override suspend fun getSavedRecipesByDate(): DataResult<List<RecipeEntity>> {
        return withContext(ioDispatcher) {
            try {
                val recipes = recipesDao.getSavedRecipesByDate()
                DataResult.Success(recipes)
            } catch (e: Exception) {
                DataResult.Error(e)
            }
        }
    }
}