package com.serranoie.android.core.data.remote

import com.serranoie.android.core.data.remote.dto.instructions.InstructionsDto
import com.serranoie.android.core.data.remote.dto.instructions.InstructionsDtoItem
import com.serranoie.android.core.data.remote.dto.recipe.RecipeDto
import com.serranoie.android.core.data.remote.dto.recipe.RecipesResponseDto
import com.serranoie.android.core.data.remote.dto.search.RecipeSearchDto
import com.serranoie.android.core.domain.model.recipe.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApi {

    @GET("/recipes/random?number=35")
    fun getRecipes(): Call<RecipesResponseDto>

    @GET("/recipes/complexSearch?sort=popularity&number=20")
    fun getPopularRecipes(): Call<RecipeSearchDto>

    @GET("/recipes/{id}/information")
    fun getRecipeDetails(@Path("id") id: Int): Call<RecipeDto>

    @GET("recipes/complexSearch")
    fun searchRecipes(
        @Query("query") query: String,
        @Query("maxFat") maxFat: Int? = null,
        @Query("number") number: Int = 20
    ): Call<RecipeSearchDto>

    @GET("recipes/{id}/analyzedInstructions")
    fun getRecipeInstructions(@Path("id") id: Int): Call<InstructionsDto>
}