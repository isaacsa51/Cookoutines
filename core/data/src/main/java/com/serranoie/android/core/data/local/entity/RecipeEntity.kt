package com.serranoie.android.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "aggregate_likes") val aggregateLikes: Int?,
    @ColumnInfo(name = "cheap") val cheap: Boolean?,
    @ColumnInfo(name = "cooking_minutes") val cookingMinutes: Int?,
    @ColumnInfo(name = "credits_text") val creditsText: String?,
    @ColumnInfo(name = "dairy_free") val dairyFree: Boolean?,
    @ColumnInfo(name = "gaps") val gaps: String?,
    @ColumnInfo(name = "gluten_free") val glutenFree: Boolean?,
    @ColumnInfo(name = "health_score") val healthScore: Int?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "image_type") val imageType: String?,
    @ColumnInfo(name = "instructions") val instructions: String?,
    @ColumnInfo(name = "low_fodmap") val lowFodmap: Boolean?,
    @ColumnInfo(name = "preparation_minutes") val preparationMinutes: Int?,
    @ColumnInfo(name = "price_per_serving") val pricePerServing: Double?,
    @ColumnInfo(name = "ready_in_minutes") val readyInMinutes: Int?,
    @ColumnInfo(name = "servings") val servings: Int?,
    @ColumnInfo(name = "source_name") val sourceName: String?,
    @ColumnInfo(name = "source_url") val sourceUrl: String?,
    @ColumnInfo(name = "spoonacular_score") val spoonacularScore: Double?,
    @ColumnInfo(name = "spoonacular_source_url") val spoonacularSourceUrl: String?,
    @ColumnInfo(name = "summary") val summary: String?,
    @ColumnInfo(name = "sustainable") val sustainable: Boolean?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "vegan") val vegan: Boolean?,
    @ColumnInfo(name = "vegetarian") val vegetarian: Boolean?,
    @ColumnInfo(name = "very_healthy") val veryHealthy: Boolean?,
    @ColumnInfo(name = "very_popular") val veryPopular: Boolean?,
    @ColumnInfo(name = "weight_watcher_smart_points") val weightWatcherSmartPoints: Int?,
    @ColumnInfo(name = "is_saved") val isSaved: Boolean = false,
    @ColumnInfo(name = "saved_date") val savedDate: Long? = null
)