package com.serranoie.android.core.data.remote.dto.instructions


import com.google.gson.annotations.SerializedName

data class Equipment(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("temperature")
    val temperature: Temperature
)