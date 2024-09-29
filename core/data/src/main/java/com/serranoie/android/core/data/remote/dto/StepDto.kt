package com.serranoie.android.core.data.remote.dto


import com.google.gson.annotations.SerializedName

data class StepDto(
    @SerializedName("equipment")
    val equipment: List<EquipmentDto?>?,
    @SerializedName("ingredients")
    val ingredients: List<IngredientDto?>?,
    @SerializedName("length")
    val length: LengthDto?,
    @SerializedName("number")
    val number: Int?,
    @SerializedName("step")
    val step: String?
)