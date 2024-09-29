package com.serranoie.android.core.data.mappers

import com.serranoie.android.core.data.remote.dto.AnalyzedInstructionDto
import com.serranoie.android.core.data.remote.dto.EquipmentDto
import com.serranoie.android.core.data.remote.dto.ExtendedIngredientDto
import com.serranoie.android.core.data.remote.dto.IngredientDto
import com.serranoie.android.core.data.remote.dto.LengthDto
import com.serranoie.android.core.data.remote.dto.MeasuresDto
import com.serranoie.android.core.data.remote.dto.MetricDto
import com.serranoie.android.core.data.remote.dto.RecipeDto
import com.serranoie.android.core.data.remote.dto.StepDto
import com.serranoie.android.core.data.remote.dto.UsDto
import com.serranoie.android.core.domain.model.recipe.AnalyzedInstruction
import com.serranoie.android.core.domain.model.recipe.Equipment
import com.serranoie.android.core.domain.model.recipe.ExtendedIngredient
import com.serranoie.android.core.domain.model.recipe.Ingredient
import com.serranoie.android.core.domain.model.recipe.Length
import com.serranoie.android.core.domain.model.recipe.Measures
import com.serranoie.android.core.domain.model.recipe.Metric
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.model.recipe.Step
import com.serranoie.android.core.domain.model.recipe.Us

fun RecipeDto.toDomain(): Recipe {
    return Recipe(
        aggregateLikes = aggregateLikes,
        analyzedInstructions = analyzedInstructions?.map { it?.toDomain() },
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
        lowFodmap = lowFodmap,
        occasions = occasions,
        originalId = originalId,
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
        weightWatcherSmartPoints = weightWatcherSmartPoints
    )
}

fun AnalyzedInstructionDto.toDomain(): AnalyzedInstruction {
    return AnalyzedInstruction(
        name = name,
        steps = steps?.map { it?.toDomain() }
    )
}

fun StepDto.toDomain(): Step {
    return Step(
        equipment = equipment?.map { it?.toDomain() },
        ingredients = ingredients?.map { it?.toDomain() },
        number = number,
        step = step,
        length = length?.toDomain()
    )
}

fun EquipmentDto.toDomain(): Equipment {
    return Equipment(
        id = id,
        image = image,
        localizedName = localizedName,
        name = name
    )
}

fun IngredientDto.toDomain(): Ingredient {
    return Ingredient(
        id = id,
        image = image,
        localizedName = localizedName,
        name = name
    )
}

fun LengthDto.toDomain(): Length {
    return Length(
        number = number,
        unit = unit
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
        nameClean = nameClean,
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
