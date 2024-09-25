package com.serranoie.android.core.data.remote.dto


import com.google.gson.annotations.SerializedName

data class ProductMatcheDto(
    @SerializedName("averageRating")
    val averageRating: Double?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("link")
    val link: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("ratingCount")
    val ratingCount: Double?,
    @SerializedName("score")
    val score: Double?,
    @SerializedName("title")
    val title: String?
)