package com.serranoie.android.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RecipeDto(
    @SerializedName("analyzedInstructions")
    val analyzedInstructions: List<Any?>?,
    @SerializedName("cheap")
    val cheap: Boolean?,
    @SerializedName("cookingMinutes")
    val cookingMinutes: Int?,
    @SerializedName("creditsText")
    val creditsText: String?,
    @SerializedName("cuisines")
    val cuisines: List<Any?>?,
    @SerializedName("dairyFree")
    val dairyFree: Boolean?,
    @SerializedName("diets")
    val diets: List<Any?>?,
    @SerializedName("dishTypes")
    val dishTypes: List<String?>?,
    @SerializedName("extendedIngredients")
    val extendedIngredients: List<ExtendedIngredientDto?>?,
    @SerializedName("gaps")
    val gaps: String?,
    @SerializedName("glutenFree")
    val glutenFree: Boolean?,
    @SerializedName("healthScore")
    val healthScore: Double?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("imageType")
    val imageType: String?,
    @SerializedName("instructions")
    val instructions: String?,
    @SerializedName("ketogenic")
    val ketogenic: Boolean?,
    @SerializedName("license")
    val license: String?,
    @SerializedName("lowFodmap")
    val lowFodmap: Boolean?,
    @SerializedName("occasions")
    val occasions: List<Any?>?,
    @SerializedName("preparationMinutes")
    val preparationMinutes: Int?,
    @SerializedName("pricePerServing")
    val pricePerServing: Double?,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int?,
    @SerializedName("servings")
    val servings: Int?,
    @SerializedName("sourceName")
    val sourceName: String?,
    @SerializedName("sourceUrl")
    val sourceUrl: String?,
    @SerializedName("spoonacularScore")
    val spoonacularScore: Double?,
    @SerializedName("spoonacularSourceUrl")
    val spoonacularSourceUrl: String?,
    @SerializedName("summary")
    val summary: String?,
    @SerializedName("sustainable")
    val sustainable: Boolean?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("vegan")
    val vegan: Boolean?,
    @SerializedName("vegetarian")
    val vegetarian: Boolean?,
    @SerializedName("veryHealthy")
    val veryHealthy: Boolean?,
    @SerializedName("veryPopular")
    val veryPopular: Boolean?,
    @SerializedName("weightWatcherSmartPoints")
    val weightWatcherSmartPoints: Int?,
    @SerializedName("whole30")
    val whole30: Boolean?,
    @SerializedName("winePairing")
    val winePairing: WinePairingDto?
)