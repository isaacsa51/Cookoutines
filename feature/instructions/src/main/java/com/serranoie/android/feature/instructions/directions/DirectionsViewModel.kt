package com.serranoie.android.feature.instructions.directions

import androidx.lifecycle.ViewModel
import com.serranoie.android.core.domain.model.instructions.InstructionsItem
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.instructions.domain.usecase.GetDetailedInstructionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class DirectionsViewModel @Inject constructor(
    private val getDetailedInstructionsUseCase: GetDetailedInstructionsUseCase
) : ViewModel() {

    private val _instructions =
        BehaviorSubject.createDefault<DataResult<List<InstructionsItem>>>(DataResult.Loading)
    val instructions: Observable<DataResult<List<InstructionsItem>>> = _instructions.hide()

    private val compositeDisposable = CompositeDisposable()

    fun getRecipeInstructions(recipeId: Int) {
        compositeDisposable.add(
            getDetailedInstructionsUseCase(recipeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> _instructions.onNext(result) },
                    { error -> _instructions.onNext(DataResult.Error(error)) }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}