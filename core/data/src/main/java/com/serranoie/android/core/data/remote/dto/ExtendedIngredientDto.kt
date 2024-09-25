package com.serranoie.android.core.data.remote.dto


import com.google.gson.annotations.SerializedName

data class ExtendedIngredientDto(
    @SerializedName("aisle")
    val aisle: String?,
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("consistency")
    val consistency: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("measures")
    val measures: MeasuresDto?,
    @SerializedName("meta")
    val meta: List<String?>?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("original")
    val original: String?,
    @SerializedName("originalName")
    val originalName: String?,
    @SerializedName("unit")
    val unit: String?
)