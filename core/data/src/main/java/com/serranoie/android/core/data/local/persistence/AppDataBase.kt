package com.serranoie.android.core.data.local.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.serranoie.android.core.data.local.dao.RecipesDao
import com.serranoie.android.core.data.local.entity.RecipeEntity
import com.serranoie.android.core.data.utils.StringListConverter

@Database(entities = [RecipeEntity::class], version = 1, exportSchema = false)
@TypeConverters(StringListConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}