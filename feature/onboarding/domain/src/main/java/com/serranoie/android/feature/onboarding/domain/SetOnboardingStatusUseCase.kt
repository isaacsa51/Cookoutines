package com.serranoie.android.feature.onboarding.domain

import com.serranoie.android.data.local.persistence.DataStoreManager
import javax.inject.Inject

class SetOnboardingCompletedUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    suspend operator fun invoke(completed: Boolean) {
        dataStoreManager.setOnboardingCompleted(completed)
    }
}