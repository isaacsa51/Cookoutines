package com.serranoie.android.core.domain.model.instructions

data class Step(
    val equipment: List<Equipment?>,
    val ingredients: List<Ingredient?>,
    val length: Length?,
    val number: Int?,
    val step: String?
)