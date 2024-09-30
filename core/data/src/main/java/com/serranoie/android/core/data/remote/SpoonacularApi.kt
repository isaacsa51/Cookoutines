package com.serranoie.android.core.data.remote

import com.serranoie.android.core.data.remote.dto.RecipesResponseDto
import retrofit2.Call
import retrofit2.http.GET

interface SpoonacularApi {

    @GET("/recipes/random?number=20")
    fun getRecipes(): Call<RecipesResponseDto>

    @GET("/recipes/complexSearch&sort=popularity&number=10")
    fun getPopularRecipes(): Call<RecipesResponseDto>
}