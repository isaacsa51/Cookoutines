package com.serranoie.android.core.domain.model.instructions

data class Equipment(
    val id: Int,
    val image: String,
    val name: String,
    val temperature: Temperature
)