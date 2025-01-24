package com.serranoie.android.feature.instructions

import androidx.lifecycle.ViewModel
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.core.domain.usecase.DeleteRecipeUseCase
import com.serranoie.android.core.domain.usecase.SaveRecipeUseCase
import com.serranoie.android.feature.instructions.domain.usecase.GetRecipeByIdUseCase
import com.serranoie.android.feature.saved.domain.usecases.GetSavedRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class InstructionsRecipeViewModel @Inject constructor(
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val getSavedRecipesUseCase: GetSavedRecipesUseCase,
    private val saveRecipeUseCase: SaveRecipeUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase
) : ViewModel() {

    private val _recipeState = BehaviorSubject.createDefault<DataResult<Recipe>>(DataResult.Loading)
    val recipeState: Observable<DataResult<Recipe>> = _recipeState.hide()

    private val compositeDisposable = CompositeDisposable()

    fun saveRecipe(recipe: Recipe) {
        compositeDisposable.add(
            Completable.fromAction { saveRecipeUseCase(recipe.copy(isSaved = true)) }
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
    }

    fun deleteRecipe(recipeId: Int) {
        compositeDisposable.add(
            Completable.fromAction { deleteRecipeUseCase(recipeId) }
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
    }

    fun isRecipeSaved(recipeId: Int): Single<Boolean> {
        return getSavedRecipesUseCase()
            .map { result ->
                when (result) {
                    is DataResult.Success -> result.data.any { it.id == recipeId && it.isSaved == true }
                    else -> false
                }
            }
    }

    fun getCurrentRecipe(id: Int) {
        compositeDisposable.add(
            getRecipeByIdUseCase(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> _recipeState.onNext(result) },
                    { error -> _recipeState.onNext(DataResult.Error(error)) }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}