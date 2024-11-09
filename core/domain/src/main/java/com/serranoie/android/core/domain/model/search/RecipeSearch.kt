package com.serranoie.android.core.domain.model.search

data class RecipeSearch(
    val number: Int?,
    val offset: Int?,
    val results: List<Result?>?,
    val totalResults: Int?
)