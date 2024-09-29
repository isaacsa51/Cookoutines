package com.serranoie.android.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RecipesResponseDto(
    @SerializedName("recipes")
    val recipes: List<RecipeDto>
)