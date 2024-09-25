package com.serranoie.android.data.remote

import com.serranoie.android.data.remote.dto.RecipeDto
import retrofit2.Call
import retrofit2.http.GET

interface SpoonacularApi {

    @GET("/recipes/informationBulk")
    fun getRecipes(): Call<List<RecipeDto>>
}