package com.serranoie.android.core.data.remote.dto


import com.google.gson.annotations.SerializedName

data class AnalyzedInstructionDto(
    @SerializedName("name")
    val name: String?,
    @SerializedName("steps")
    val steps: List<StepDto?>?
)