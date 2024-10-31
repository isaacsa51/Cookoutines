package com.serranoie.android.feature.onboarding.domain.usecase

import javax.inject.Inject

class GetOnboardingStatusUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    operator fun invoke(): Flow<Boolean> = dataStoreManager.hasCompletedOnboarding()
}