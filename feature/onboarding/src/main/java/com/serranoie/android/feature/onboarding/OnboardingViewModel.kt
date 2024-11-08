package com.serranoie.android.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.feature.onboarding.domain.GetOnboardingStatusUseCase
import com.serranoie.android.feature.onboarding.domain.SetOnboardingCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getOnboardingStatusUseCase: GetOnboardingStatusUseCase,
    private val setOnboardingCompletedUseCase: SetOnboardingCompletedUseCase
) : ViewModel() {

    private val _onboardingCompleted = MutableStateFlow(false)
    val onboardingCompleted: StateFlow<Boolean> = _onboardingCompleted

    init {
        viewModelScope.launch {
            getOnboardingStatusUseCase().collect { status ->
                _onboardingCompleted.value = status
            }
        }
    }

    fun setOnboardingCompleted() {
        viewModelScope.launch {
            setOnboardingCompletedUseCase(true)
            _onboardingCompleted.value = true
        }
    }
}