package com.serranoie.android.core.data.local.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.serranoie.android.core.data.local.dao.RecipesDao
import com.serranoie.android.core.data.local.entity.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}