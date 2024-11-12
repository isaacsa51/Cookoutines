package com.serranoie.android.core.data.mappers

import com.serranoie.android.core.data.remote.dto.instructions.InstructionsDto
import com.serranoie.android.core.domain.model.instructions.Instructions

fun InstructionsDto.toDomain(): List<Instructions> {
    return this.map { dtoItem ->
        Instructions(
            name = dtoItem.name,
            steps = dtoItem.steps.map { step ->
                Step(
                    equipment = step.equipment.map { it.toDomainModel() },
                    ingredients = step.ingredients.map { it.toDomainModel() },
                    length = step.length.toDomainModel(),
                    number = step.number,
                    step = step.step
                )
            }
        )
    }
}