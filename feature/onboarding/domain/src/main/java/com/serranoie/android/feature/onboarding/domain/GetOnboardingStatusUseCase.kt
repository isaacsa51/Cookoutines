package com.serranoie.android.feature.onboarding.domain

import com.serranoie.android.data.local.persistence.DataStoreManager
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class GetOnboardingStatusUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    operator fun invoke(): Flowable<Boolean> = dataStoreManager.hasCompletedOnboarding()
}
