package com.serranoie.android.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.serranoie.android.feature.onboarding.domain.GetOnboardingStatusUseCase
import com.serranoie.android.feature.onboarding.domain.SetOnboardingCompletedUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingViewModelFactory @Inject constructor(
    private val getOnboardingUseCase: GetOnboardingStatusUseCase,
    private val saveOnboardingUseCase: SetOnboardingCompletedUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
            return OnboardingViewModel(getOnboardingUseCase, saveOnboardingUseCase) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}