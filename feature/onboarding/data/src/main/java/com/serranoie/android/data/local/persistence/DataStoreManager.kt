package com.serranoie.android.data.local.persistence

import android.content.Context
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = RxPreferenceDataStoreBuilder(context, "onboarding_prefs").build()
    private val pref_error = emptyPreferences()

    private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")

    fun hasCompletedOnboarding(): Flowable<Boolean> {
        return dataStore.data()
            .onErrorResumeNext { exception: Throwable ->
                if (exception is IOException) {
                    Flowable.just(emptyPreferences())
                } else {
                    Flowable.error(exception)
                }
            }
            .map { preferences: Preferences ->
                preferences[ONBOARDING_COMPLETED_KEY] ?: false
            }
    }

    fun setOnboardingCompleted(completed: Boolean): Completable {
        val PREF_KEY = ONBOARDING_COMPLETED_KEY
        return dataStore.updateDataAsync { prefsIn ->
            val mutablePreferences = prefsIn.toMutablePreferences()
            mutablePreferences[PREF_KEY] = completed
            Single.just(mutablePreferences)
        }.onErrorReturnItem(pref_error).ignoreElement()
    }
}