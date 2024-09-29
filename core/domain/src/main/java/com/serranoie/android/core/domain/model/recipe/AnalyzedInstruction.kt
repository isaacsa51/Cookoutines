package com.serranoie.android.core.domain.model.recipe

data class AnalyzedInstruction(
    val name: String?,
    val steps: List<Step?>?
)