package com.serranoie.android.core.data.mappers

import com.serranoie.android.core.data.remote.dto.ExtendedIngredientDto
import com.serranoie.android.core.data.remote.dto.MeasuresDto
import com.serranoie.android.core.data.remote.dto.MetricDto
import com.serranoie.android.core.data.remote.dto.ProductMatcheDto
import com.serranoie.android.core.data.remote.dto.RecipeDto
import com.serranoie.android.core.data.remote.dto.UsDto
import com.serranoie.android.core.data.remote.dto.WinePairingDto
import com.serranoie.android.core.domain.model.recipe.ExtendedIngredient
import com.serranoie.android.core.domain.model.recipe.Measures
import com.serranoie.android.core.domain.model.recipe.Metric
import com.serranoie.android.core.domain.model.recipe.ProductMatche
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.model.recipe.Us
import com.serranoie.android.core.domain.model.recipe.WinePairing

fun RecipeDto.toDomain(): Recipe {
    return Recipe(
        analyzedInstructions = analyzedInstructions,
        cheap = cheap,
        cookingMinutes = cookingMinutes,
        creditsText = creditsText,
        cuisines = cuisines,
        dairyFree = dairyFree,
        diets = diets,
        dishTypes = dishTypes,
        extendedIngredients = extendedIngredients?.map { it?.toDomain() },
        gaps = gaps,
        glutenFree = glutenFree,
        healthScore = healthScore,
        id = id,
        image = image,
        imageType = imageType,
        instructions = instructions,
        ketogenic = ketogenic,
        license = license,
        lowFodmap = lowFodmap,
        occasions = occasions,
        preparationMinutes = preparationMinutes,
        pricePerServing = pricePerServing,
        readyInMinutes = readyInMinutes,
        servings = servings,
        sourceName = sourceName,
        sourceUrl = sourceUrl,
        spoonacularScore = spoonacularScore,
        spoonacularSourceUrl = spoonacularSourceUrl,
        summary = summary,
        sustainable = sustainable,
        title = title,
        vegan = vegan,
        vegetarian = vegetarian,
        veryHealthy = veryHealthy,
        veryPopular = veryPopular,
        weightWatcherSmartPoints = weightWatcherSmartPoints,
        whole30 = whole30,
        winePairing = winePairing?.toDomain()
    )
}

fun ExtendedIngredientDto.toDomain(): ExtendedIngredient {
    return ExtendedIngredient(
        aisle = aisle,
        amount = amount,
        consistency = consistency,
        id = id,
        image = image,
        measures = measures?.toDomain(),
        meta = meta,
        name = name,
        original = original,
        originalName = originalName,
        unit = unit
    )
}

fun MeasuresDto.toDomain(): Measures {
    return Measures(
        metric = metric?.toDomain(),
        us = us?.toDomain()
    )
}

fun MetricDto.toDomain(): Metric {
    return Metric(
        amount = amount,
        unitLong = unitLong,
        unitShort = unitShort
    )
}

fun UsDto.toDomain(): Us {
    return Us(
        amount = amount,
        unitLong = unitLong,
        unitShort = unitShort
    )
}

fun WinePairingDto.toDomain(): WinePairing {
    return WinePairing(
        pairedWines = pairedWines,
        pairingText = pairingText,
        productMatches = productMatches?.map { it?.toDomain() }
    )
}

fun ProductMatcheDto.toDomain(): ProductMatche {
    return ProductMatche(
        averageRating = averageRating,
        description = description,
        id = id,
        imageUrl = imageUrl,
        link = link,
        price = price,
        ratingCount = ratingCount,
        score = score,
        title = title
    )
}