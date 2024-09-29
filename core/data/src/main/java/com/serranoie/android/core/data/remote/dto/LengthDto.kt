package com.serranoie.android.core.data.remote.dto


import com.google.gson.annotations.SerializedName

data class LengthDto(
    @SerializedName("number")
    val number: Int?,
    @SerializedName("unit")
    val unit: String?
)