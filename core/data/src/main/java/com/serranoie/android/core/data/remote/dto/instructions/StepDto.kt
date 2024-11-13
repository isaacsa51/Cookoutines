package com.serranoie.android.core.data.remote.dto.instructions

import com.google.gson.annotations.SerializedName

data class StepDto(
  @SerializedName("equipment")
    val equipment: List<EquipmentDto?>? = listOf(),
  @SerializedName("ingredients")
    val ingredients: List<IngredientDto?>? = listOf(),
  @SerializedName("length")
    val length: LengthDto? = LengthDto(),
  @SerializedName("number")
    val number: Int? = 0,
  @SerializedName("step")
    val step: String? = ""
)