package com.serranoie.android.core.data.remote.repository

import com.serranoie.android.core.data.local.dao.RecipesDao
import com.serranoie.android.core.data.mappers.toDomain
import com.serranoie.android.core.data.mappers.toEntity
import com.serranoie.android.core.data.mappers.toListDomain
import com.serranoie.android.core.data.mappers.toRecipe
import com.serranoie.android.core.data.remote.SpoonacularApi
import com.serranoie.android.core.domain.model.instructions.InstructionsItem
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val api: SpoonacularApi,
    private val recipesDao: RecipesDao,
) : SpoonacularRepository {

    override fun getRandomRecipes(): Single<DataResult<List<Recipe>>> {
        return Single.fromCallable {
            val response = api.getRecipes().execute()

            if (response.isSuccessful) {
                val recipes = response.body()?.recipes?.map { it.toDomain() } ?: emptyList()

                DataResult.Success(recipes)
            } else {
                DataResult.Error(Exception("API error: ${response.code()}"))
            }
        }
    }

    override fun getRecipeById(id: Int): Single<DataResult<Recipe>> {
        return Single.fromCallable {
            val response = api.getRecipeDetails(id).execute()

            if (response.isSuccessful) {
                val recipe = response.body()?.toDomain()

                DataResult.Success(recipe!!)
            } else {
                DataResult.Error(Exception("API request failed"))
            }
        }
    }

    override fun getPopularRecipes(): Single<DataResult<List<Result>>> {
        return Single.fromCallable {
            val response = api.getPopularRecipes().execute()

            if (response.isSuccessful) {
                val results = response.body()?.results.orEmpty()
                val recipes = results.mapNotNull { it?.toDomain() }

                DataResult.Success(recipes)
            } else {
                DataResult.Error(Exception("API request failed: ${response.message()}"))
            }
        }
    }

    override fun searchRecipes(query: String): Single<DataResult<List<Result>>> {
        return Single.fromCallable {
            val response = api.searchRecipes(query).execute()

            if (response.isSuccessful) {
                val resultsDtoList = response.body()?.results.orEmpty()
                val recipes = resultsDtoList.mapNotNull { it?.toDomain() }

                DataResult.Success(recipes)
            } else {
                DataResult.Error(Exception("API request failed: ${response.message()}"))
            }
        }
    }

    override fun getRecipeInstructions(id: Int): Single<DataResult<List<InstructionsItem>>> {
        return Single.fromCallable {
            val response = api.getRecipeInstructions(id).execute()

            if (response.isSuccessful) {
                val responseBody = response.body()
                try {
                    val mappedData = responseBody?.toListDomain()
                    DataResult.Success(mappedData!!)
                } catch (e: Exception) {
                    DataResult.Error(e)
                }
            } else {
                DataResult.Error(Exception("API request failed: ${response.message()}"))
            }
        }
    }

    override fun insertRecipe(recipe: Recipe) {
        val data = recipe.toEntity()

        recipesDao.insertRecipe(data)
    }

    override fun deleteRecipe(id: Int) {
        recipesDao.deleteRecipe(id)
    }

    override fun getSavedRecipesByDate(): Single<DataResult<List<Recipe>>> {
        return recipesDao.getSavedRecipesByDate()
            .map<DataResult<List<Recipe>>> { recipes ->
                DataResult.Success(recipes.map { it.toRecipe() })
            }
            .onErrorReturn { exception -> DataResult.Error(exception) }
    }
}