package com.serranoie.android.feature.recipes_list.adapter

import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.model.search.Result

sealed class RecipeListItem {
    data class TrendingRecipeItem(val recipe: Result) : RecipeListItem()
    data class LatestRecipeItem(val recipe: Recipe) : RecipeListItem()
}