package com.serranoie.android.core.data.remote.dto.instructions


import com.google.gson.annotations.SerializedName

data class LengthDto(
    @SerializedName("number")
    val number: Int? = 0,
    @SerializedName("unit")
    val unit: String? = ""
)