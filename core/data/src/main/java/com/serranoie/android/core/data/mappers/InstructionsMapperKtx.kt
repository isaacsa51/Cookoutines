package com.serranoie.android.core.data.mappers

import com.serranoie.android.core.data.remote.dto.instructions.Equipment
import com.serranoie.android.core.data.remote.dto.instructions.Ingredient
import com.serranoie.android.core.data.remote.dto.instructions.InstructionsDto
import com.serranoie.android.core.data.remote.dto.instructions.Length
import com.serranoie.android.core.data.remote.dto.instructions.Step
import com.serranoie.android.core.data.remote.dto.instructions.Temperature
import com.serranoie.android.core.domain.model.instructions.Instructions
import com.serranoie.android.core.domain.model.instructions.InstructionsItem

fun InstructionsDto.toDomain(): Instructions {
    val instructions = Instructions()
    instructions.addAll(
        this.map { instructionsDtoItem ->
            InstructionsItem(
                name = instructionsDtoItem.name,
                steps = instructionsDtoItem.steps.map { it.toDomain() }
            )
        }
    )
    return instructions
}

fun InstructionsDto.toListDomain(): List<Instructions> {
    return this.map { instructionsDtoItem ->
        Instructions().apply {
            add(
                InstructionsItem(
                    name = instructionsDtoItem.name,
                    steps = instructionsDtoItem.steps.map { it.toDomain() }
                )
            )
        }
    }
}

fun Step.toDomain(): com.serranoie.android.core.domain.model.instructions.Step {
    return com.serranoie.android.core.domain.model.instructions.Step(
        equipment = equipment.map { it.toDomain() },
        ingredients = ingredients.map { it.toDomain() },
        length = length.toDomain(),
        number = number,
        step = step
    )
}

fun Length.toDomain(): com.serranoie.android.core.domain.model.instructions.Length {
    return com.serranoie.android.core.domain.model.instructions.Length(
        number = number,
        unit = unit
    )
}


fun Equipment.toDomain(): com.serranoie.android.core.domain.model.instructions.Equipment {
    return com.serranoie.android.core.domain.model.instructions.Equipment(
        id = id,
        image = image,
        name = name,
        temperature = temperature.toDomain(),
    )
}

fun Temperature.toDomain(): com.serranoie.android.core.domain.model.instructions.Temperature {
    return com.serranoie.android.core.domain.model.instructions.Temperature(
        number = number,
        unit = unit
    )
}

fun Ingredient.toDomain(): com.serranoie.android.core.domain.model.instructions.Ingredient {
    return com.serranoie.android.core.domain.model.instructions.Ingredient(
        id = id,
        image = image,
        name = name,
    )
}