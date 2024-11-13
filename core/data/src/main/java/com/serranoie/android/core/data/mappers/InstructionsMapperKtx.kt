package com.serranoie.android.core.data.mappers

import com.serranoie.android.core.data.remote.dto.instructions.EquipmentDto
import com.serranoie.android.core.data.remote.dto.instructions.IngredientDto
import com.serranoie.android.core.data.remote.dto.instructions.InstructionsDto
import com.serranoie.android.core.data.remote.dto.instructions.InstructionsDtoItem
import com.serranoie.android.core.data.remote.dto.instructions.LengthDto
import com.serranoie.android.core.data.remote.dto.instructions.StepDto
import com.serranoie.android.core.data.remote.dto.instructions.TemperatureDto
import com.serranoie.android.core.domain.model.instructions.Equipment
import com.serranoie.android.core.domain.model.instructions.Ingredient
import com.serranoie.android.core.domain.model.instructions.Instructions
import com.serranoie.android.core.domain.model.instructions.InstructionsItem
import com.serranoie.android.core.domain.model.instructions.Length
import com.serranoie.android.core.domain.model.instructions.Step
import com.serranoie.android.core.domain.model.instructions.Temperature

fun InstructionsDto.toDomain(): Instructions {
    return Instructions().apply {
        this@toDomain.forEach { instructionsDtoItem ->
            add(
                InstructionsItem(
                    name = instructionsDtoItem.name ?: "",
                    steps = instructionsDtoItem.step?.mapNotNull { it?.toDomain() } ?: emptyList()
                )
            )
        }
    }
}

fun InstructionsDtoItem.toDomain(): InstructionsItem {
    return InstructionsItem(
        name = name ?: "",
        steps = step?.mapNotNull { it?.toDomain() } ?: emptyList()
    )
}

fun InstructionsDto.toListDomain(): List<InstructionsItem> {
    return this.map { dto ->
        InstructionsItem(
            name = dto.name ?: "",
            steps = dto.step?.mapNotNull { it?.toDomain() } ?: emptyList()
        )
    }
}

fun StepDto.toDomain(): Step {
    return Step(
        equipment = equipment?.mapNotNull { it?.toDomain() } ?: emptyList(),
        ingredients = ingredients?.mapNotNull { it?.toDomain() } ?: emptyList(),
        length = length?.toDomain(),
        number = number,
        step = step ?: ""
    )
}

fun LengthDto.toDomain(): Length {
    return Length(
        number = number,
        unit = unit ?: ""
    )
}

fun EquipmentDto.toDomain(): Equipment {
    return Equipment(
        id = id,
        image = image ?: "",
        name = name ?: "",
        temperature = temperature?.toDomain()
    )
}

fun TemperatureDto.toDomain(): Temperature {
    return Temperature(
        number = number,
        unit = unit ?: ""
    )
}

fun IngredientDto.toDomain(): Ingredient {
    return Ingredient(
        id = id,
        image = image ?: "",
        name = name ?: "",
    )
}