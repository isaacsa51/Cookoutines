package com.serranoie.android.core.domain.model.recipe

data class WinePairing(
    val pairedWines: List<String?>?,
    val pairingText: String?,
    val productMatches: List<ProductMatche?>?
)