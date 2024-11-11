package com.serranoie.android.core.domain.model.recipe

import java.io.Serializable

data class Measures(
    val metric: Metric?,
    val us: Us?
) : Serializable