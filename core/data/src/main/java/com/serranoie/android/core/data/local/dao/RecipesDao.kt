package com.serranoie.android.core.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.serranoie.android.core.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

interface RecipesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM recipe WHERE is_saved = 1 ORDER BY saved_date DESC")
    fun getSavedRecipesByDate(): Flow<List<RecipeEntity>>
}