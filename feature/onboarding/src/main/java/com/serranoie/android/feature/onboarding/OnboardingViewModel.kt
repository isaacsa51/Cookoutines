package com.serranoie.android.feature.onboarding

import androidx.lifecycle.ViewModel
import com.serranoie.android.feature.onboarding.domain.GetOnboardingStatusUseCase
import com.serranoie.android.feature.onboarding.domain.SetOnboardingCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getOnboardingStatusUseCase: GetOnboardingStatusUseCase,
    private val setOnboardingCompletedUseCase: SetOnboardingCompletedUseCase
) : ViewModel() {

    private val _onboardingCompleted = BehaviorSubject.create<Boolean>()
    val onboardingCompleted: Observable<Boolean> = _onboardingCompleted.hide()
    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(
            getOnboardingStatusUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { status ->
                    _onboardingCompleted.onNext(status)
                }
        )
    }

    fun setOnboardingCompleted() {
        compositeDisposable.add(
            setOnboardingCompletedUseCase(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _onboardingCompleted.onNext(true)
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}