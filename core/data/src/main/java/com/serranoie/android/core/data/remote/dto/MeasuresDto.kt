package com.serranoie.android.core.data.remote.dto


import com.google.gson.annotations.SerializedName

data class MeasuresDto(
    @SerializedName("metric")
    val metric: MetricDto?,
    @SerializedName("us")
    val us: UsDto?
)