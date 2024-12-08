package com.serranoie.android.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.serranoie.android.core.data.local.dao.RecipesDao
import com.serranoie.android.core.data.local.persistence.AppDataBase
import com.serranoie.android.core.data.remote.SpoonacularApi
import com.serranoie.android.core.data.remote.repository.RecipeRepositoryImpl
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.usecase.DeleteRecipeUseCase
import com.serranoie.android.core.domain.usecase.SaveRecipeUseCase
import com.serranoie.android.data.local.persistence.DataStoreManager
import com.serranoie.android.feature.instructions.domain.usecase.GetDetailedInstructionsUseCase
import com.serranoie.android.feature.instructions.domain.usecase.GetRecipeByIdUseCase
import com.serranoie.android.feature.onboarding.domain.GetOnboardingStatusUseCase
import com.serranoie.android.feature.onboarding.domain.SetOnboardingCompletedUseCase
import com.serranoie.android.feature.recipes_list.domain.usecase.GetPopularRecipesUseCase
import com.serranoie.android.feature.recipes_list.domain.usecase.GetRandomRecipesUseCase
import com.serranoie.android.feature.recipes_list.domain.usecase.SearchRecipeUseCase
import com.serranoie.android.feature.saved.domain.usecases.GetSavedRecipesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DependenciesProvider {

    private const val API_KEY = BuildConfig.API_KEY
    private const val BASE_URL = BuildConfig.BASE_URL

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            if (!columnExists(db, "recipe", "is_saved")) {
                db.execSQL("ALTER TABLE recipe ADD COLUMN is_saved INTEGER NOT NULL DEFAULT 0")
            }
        }
    }

    private fun columnExists(database: SupportSQLiteDatabase, tableName: String, columnName: String): Boolean {
        val cursor = database.query("PRAGMA table_info($tableName)")
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            if (name == columnName) {
                cursor.close()
                return true
            }
        }
        cursor.close()
        return false
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val apiKeyInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val url = originalRequest.url.newBuilder()
                .addQueryParameter("apiKey", API_KEY)
                .build()
            val request = originalRequest.newBuilder()
                .url(url)
                .build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder().addInterceptor(apiKeyInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): SpoonacularApi {
        return retrofit.create(SpoonacularApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDataBase(@ApplicationContext appContext: Context): AppDataBase {
        return Room.databaseBuilder(
            appContext,
            AppDataBase::class.java,
            "app_database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideRecipeDao(appDataBase: AppDataBase): RecipesDao {
        return appDataBase.recipesDao()
    }

    @Provides
    @Singleton
    fun provideRepository(apiService: SpoonacularApi, recipesDao: RecipesDao): SpoonacularRepository {
        return RecipeRepositoryImpl(apiService, recipesDao)
    }

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }

    @Provides
    @Singleton
    fun providesRandomRecipesUseCase(repository: RecipeRepositoryImpl): GetRandomRecipesUseCase {
        return GetRandomRecipesUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesPopularRecipesUseCase(repository: RecipeRepositoryImpl): GetPopularRecipesUseCase {
        return GetPopularRecipesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetRecipeByIdUseCase(repository: RecipeRepositoryImpl): GetRecipeByIdUseCase {
        return GetRecipeByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetOnboardingStatusUseCase(dataStoreManager: DataStoreManager): GetOnboardingStatusUseCase {
        return GetOnboardingStatusUseCase(dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideSetOnboardingCompletedUseCase(dataStoreManager: DataStoreManager): SetOnboardingCompletedUseCase {
        return SetOnboardingCompletedUseCase(dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideSearchRecipeUseCAse(repository: RecipeRepositoryImpl): SearchRecipeUseCase {
        return SearchRecipeUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetDetailedInstructionsUseCase(repository: RecipeRepositoryImpl): GetDetailedInstructionsUseCase {
        return GetDetailedInstructionsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveRecipeUseCase(repository: RecipeRepositoryImpl): SaveRecipeUseCase {
        return SaveRecipeUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteRecipeUseCase(repository: RecipeRepositoryImpl): DeleteRecipeUseCase {
        return DeleteRecipeUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetSavedRecipesUseCase(repository: RecipeRepositoryImpl): GetSavedRecipesUseCase {
        return GetSavedRecipesUseCase(repository)
    }
}