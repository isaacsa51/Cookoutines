package com.serranoie.android.core.data.remote.dto.instructions


import com.google.gson.annotations.SerializedName

data class IngredientDto(
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("image")
    val image: String? = "",
    @SerializedName("localizedName")
    val localizedName: String? = "",
    @SerializedName("name")
    val name: String? = ""
)