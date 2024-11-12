package com.serranoie.android.core.data.remote.dto.instructions


import com.google.gson.annotations.SerializedName

data class InstructionsDtoItem(
    @SerializedName("name")
    val name: String,
    @SerializedName("steps")
    val steps: List<Step>
)