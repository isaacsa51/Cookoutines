package com.serranoie.android.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.serranoie.android.core.data.local.entity.RecipeEntity
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: RecipeEntity)

    @Query("DELETE FROM recipe WHERE id = :id")
    fun deleteRecipe(id: Int)

    @Query("SELECT * FROM recipe")
    fun getSavedRecipesByDate(): Single<List<RecipeEntity>>
}