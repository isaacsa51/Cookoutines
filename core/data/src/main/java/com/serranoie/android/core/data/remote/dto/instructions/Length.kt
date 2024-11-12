package com.serranoie.android.core.data.remote.dto.instructions


import com.google.gson.annotations.SerializedName

data class Length(
    @SerializedName("number")
    val number: Int,
    @SerializedName("unit")
    val unit: String
)