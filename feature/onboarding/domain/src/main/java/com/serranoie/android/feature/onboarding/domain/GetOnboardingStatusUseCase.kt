package com.serranoie.android.feature.onboarding.domain

import com.serranoie.android.data.local.persistence.DataStoreManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOnboardingStatusUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    operator fun invoke(): Flow<Boolean> = dataStoreManager.hasCompletedOnboarding()
}
