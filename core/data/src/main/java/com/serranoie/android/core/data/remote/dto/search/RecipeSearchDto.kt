package com.serranoie.android.core.data.remote.dto.search

import com.google.gson.annotations.SerializedName

data class RecipeSearchDto(
    @SerializedName("number")
    val number: Int?,
    @SerializedName("offset")
    val offset: Int?,
    @SerializedName("results")
    val results: List<ResultDto?>?,
    @SerializedName("totalResults")
    val totalResults: Int?
)