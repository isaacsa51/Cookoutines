package com.serranoie.android.core.data.remote.dto


import com.google.gson.annotations.SerializedName

data class IngredientDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("localizedName")
    val localizedName: String?,
    @SerializedName("name")
    val name: String?
)