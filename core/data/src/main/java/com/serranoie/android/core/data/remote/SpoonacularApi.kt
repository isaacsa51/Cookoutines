package com.serranoie.android.core.data.remote

import com.serranoie.android.core.data.remote.dto.RecipeDto
import retrofit2.Call
import retrofit2.http.GET

interface SpoonacularApi {

    @GET("/recipes/random?number=20")
    fun getRecipes(): Call<List<RecipeDto>>
}