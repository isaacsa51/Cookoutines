package com.serranoie.android.core.data.local.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow

class DataStoreManager(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding_prefs")

//    suspend fun setOnboardingCompleted(completed: Boolean) {
//        context.dataStore.edit { preferences ->
//            preferences[Preferences.Key<Boolean>("onboarding_completed")] = completed
//        }
//    }
//
//    fun isOnboardingCompleted(): Flow<Boolean> {
//        return context.dataStore.data.map { preferences ->
//            preferences[Preferences.Key<Boolean>("onboarding_completed")] ?: false
//        }
//    }
}