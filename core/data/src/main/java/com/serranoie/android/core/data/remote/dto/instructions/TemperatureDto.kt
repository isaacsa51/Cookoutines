package com.serranoie.android.core.data.remote.dto.instructions


import com.google.gson.annotations.SerializedName

data class TemperatureDto(
    @SerializedName("number")
    val number: Double? = 0.0,
    @SerializedName("unit")
    val unit: String? = ""
)