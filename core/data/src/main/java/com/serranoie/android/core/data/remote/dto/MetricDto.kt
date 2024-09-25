package com.serranoie.android.core.data.remote.dto


import com.google.gson.annotations.SerializedName

data class MetricDto(
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("unitLong")
    val unitLong: String?,
    @SerializedName("unitShort")
    val unitShort: String?
)