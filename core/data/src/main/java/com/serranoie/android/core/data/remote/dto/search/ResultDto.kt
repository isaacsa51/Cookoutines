package com.serranoie.android.core.data.remote.dto.search

import com.google.gson.annotations.SerializedName

data class ResultDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("imageType")
    val imageType: String?,
    @SerializedName("title")
    val title: String?
)