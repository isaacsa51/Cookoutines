package com.serranoie.android.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.feature.onboarding.domain.GetOnboardingStatusUseCase
import com.serranoie.android.feature.onboarding.domain.SetOnboardingCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    getOnboardingStatusUseCase: GetOnboardingStatusUseCase,
    private val setOnboardingCompletedUseCase: SetOnboardingCompletedUseCase
) : ViewModel() {
    val onboardingCompleted: Flow<Boolean> = getOnboardingStatusUseCase()

    fun setOnboardingCompleted() {
        viewModelScope.launch {
            setOnboardingCompletedUseCase(true)
        }
    }
}