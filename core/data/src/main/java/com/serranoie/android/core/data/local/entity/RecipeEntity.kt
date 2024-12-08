package com.serranoie.android.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import com.serranoie.android.core.data.utils.StringListConverter

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "aggregate_likes") val aggregateLikes: Int?,
    @TypeConverters(StringListConverter::class)
    @ColumnInfo(name = "analyzed_instructions")
    val analyzedInstructions: List<String?>?,
    @ColumnInfo(name = "cheap") val cheap: Boolean?,
    @ColumnInfo(name = "cooking_minutes") val cookingMinutes: Int?,
    @ColumnInfo(name = "credits_text") val creditsText: String?,
    @ColumnInfo(name = "dairy_free") val dairyFree: Boolean?,
    @ColumnInfo(name = "gaps") val gaps: String?,
    @ColumnInfo(name = "gluten_free") val glutenFree: Boolean?,
    @ColumnInfo(name = "health_score") val healthScore: Int?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "image_type") val imageType: String?,
    @ColumnInfo(name = "instructions") val instructions: String?,
    @ColumnInfo(name = "low_fodmap") val lowFodmap: Boolean?,
    @ColumnInfo(name = "preparation_minutes") val preparationMinutes: Int?,
    @ColumnInfo(name = "price_per_serving") val pricePerServing: Double?,
    @ColumnInfo(name = "ready_in_minutes") val readyInMinutes: Int?,
    @ColumnInfo(name = "servings") val servings: Int?,
    @ColumnInfo(name = "source_name") val sourceName: String?,
    @ColumnInfo(name = "source_url") val sourceUrl: String?,
    @ColumnInfo(name = "spoonacular_score") val spoonacularScore: Double?,
    @ColumnInfo(name = "spoonacular_source_url") val spoonacularSourceUrl: String?,
    @ColumnInfo(name = "summary") val summary: String?,
    @ColumnInfo(name = "sustainable") val sustainable: Boolean?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "vegan") val vegan: Boolean?,
    @ColumnInfo(name = "vegetarian") val vegetarian: Boolean?,
    @ColumnInfo(name = "very_healthy") val veryHealthy: Boolean?,
    @ColumnInfo(name = "very_popular") val veryPopular: Boolean?,
    @ColumnInfo(name = "weight_watcher_smart_points") val weightWatcherSmartPoints: Int?,
    @ColumnInfo(name = "is_saved") val isSaved: Boolean = false,
)

@Entity(tableName = "analyzed_instruction")
data class AnalyzedInstructionEntity(
    @PrimaryKey(autoGenerate = true) val analyzedInstructionId: Int = 0,
    val name: String?,
    val recipeId: Int, // Foreign key to RecipeEntity
    @Ignore val steps: List<StepEntity>? = null // Ignored, handled by relationship
)

@Entity(tableName = "step")
data class StepEntity(
    @PrimaryKey(autoGenerate = true) val stepId: Int = 0,
    val number: Int?,
    val step: String?,
    val analyzedInstructionId: Int, // Foreign key to AnalyzedInstructionEntity
    val lengthNumber: Int?, // From Length data class
    val lengthUnit: String? // From Length data class
)

@Entity(tableName = "equipment")
data class EquipmentEntity(
    @PrimaryKey val id: Int,
    val image: String?,
    val localizedName: String?,
    val name: String?
)

@Entity(tableName = "ingredient")
data class IngredientEntity(
    @PrimaryKey val id: Int,
    val image: String?,
    val localizedName: String?,
    val name: String?
)

@Entity(
    tableName = "extended_ingredient",
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExtendedIngredientEntity(
    @PrimaryKey(autoGenerate = true) val extendedIngredientId: Int = 0,
    val aisle: String?,
    val amount: Double?,
    val consistency: String?,
    val id: Int?,
    val image: String?,
    val name: String?,
    val nameClean: String?,
    val original: String?,
    val originalName: String?,
    val unit: String?,
    val recipeId: Int, // Foreign key to RecipeEntity
    // Measures data class properties can be added here if needed
)

// Define relationships using @Relation
// Recipe with Extended Ingredients (One-to-Many)
data class RecipeWihExtendedIngredients(
    @Embedded val recipe: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val extendedIngredients: List<ExtendedIngredientEntity>
)

// Analyzed Instruction with Steps (One-to-Many)
data class AnalyzedInstructionWithSteps(
    @Embedded val analyzedInstruction: AnalyzedInstructionEntity,
    @Relation(
        parentColumn = "analyzedInstructionId",
        entityColumn = "analyzedInstructionId"
    )
    val steps: List<StepEntity>
)

// Step with Equipment and Ingredients (Many-to-Many)
data class StepWithEquipmentAndIngredients(
    @Embedded val step: StepEntity,
    @Relation(
        parentColumn = "stepId",
        entityColumn = "stepId",
        associateBy = Junction(StepEquipmentCrossRef::class)
    )
    val equipment: List<EquipmentEntity>,
    @Relation(
        parentColumn = "stepId",
        entityColumn = "stepId",
        associateBy = Junction(StepIngredientCrossRef::class)
    )
    val ingredients: List<IngredientEntity>
)

// Cross-reference tables for many-to-many relationships
@Entity(primaryKeys = ["stepId", "equipmentId"])
data class StepEquipmentCrossRef(
    val stepId: Int,
    val equipmentId: Int
)

@Entity(primaryKeys = ["stepId", "ingredientId"])
data class StepIngredientCrossRef(
    val stepId: Int,
    val ingredientId: Int
)