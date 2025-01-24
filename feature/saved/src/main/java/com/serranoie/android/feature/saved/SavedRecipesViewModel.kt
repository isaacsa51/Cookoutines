package com.serranoie.android.feature.saved

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.core.domain.usecase.DeleteRecipeUseCase
import com.serranoie.android.feature.saved.domain.usecases.GetSavedRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class SavedRecipesViewModel @Inject constructor(
    application: Application,
    private val getSavedRecipesUseCase: GetSavedRecipesUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase,
    private val repository: SpoonacularRepository
) : AndroidViewModel(application) {

    private val _recipesState =
        BehaviorSubject.createDefault<DataResult<List<Recipe>>>(DataResult.Loading)
    val recipesState: Observable<DataResult<List<Recipe>>> = _recipesState.hide()

    private val _refreshTrigger = PublishSubject.create<Unit>()
    val refreshTrigger: Observable<Unit> = _refreshTrigger.hide()

    private val compositeDisposable = CompositeDisposable()

    init {
        loadSavedRecipes()
    }

    fun triggerRefresh() {
        _refreshTrigger.onNext(Unit)
    }

    fun loadSavedRecipes() {
        compositeDisposable.add(
            getSavedRecipesUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> _recipesState.onNext(result) },
                    { error -> _recipesState.onNext(DataResult.Error(error)) }
                )
        )
    }

    fun deleteRecipe(id: Int) {
        compositeDisposable.add(
            Completable.fromAction { deleteRecipeUseCase(id) }
                .subscribeOn(Schedulers.io())
                .andThen(repository.getSavedRecipesByDate())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> _recipesState.onNext(result) },
                    { error -> _recipesState.onNext(DataResult.Error(error)) }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}