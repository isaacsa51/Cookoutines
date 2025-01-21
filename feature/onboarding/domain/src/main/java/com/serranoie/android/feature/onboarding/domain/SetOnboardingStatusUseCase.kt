package com.serranoie.android.feature.onboarding.domain

import com.serranoie.android.data.local.persistence.DataStoreManager
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class SetOnboardingCompletedUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    operator fun invoke(completed: Boolean): Completable {
        return dataStoreManager.setOnboardingCompleted(completed)
    }
}