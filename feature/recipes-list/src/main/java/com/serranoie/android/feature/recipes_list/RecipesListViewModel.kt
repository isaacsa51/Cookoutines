package com.serranoie.android.feature.recipes_list

import androidx.lifecycle.ViewModel
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.core.domain.usecase.DeleteRecipeUseCase
import com.serranoie.android.core.domain.usecase.SaveRecipeUseCase
import com.serranoie.android.feature.recipes_list.domain.usecase.GetPopularRecipesUseCase
import com.serranoie.android.feature.recipes_list.domain.usecase.GetRandomRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject constructor(
    private val getRandomRecipesUseCase: GetRandomRecipesUseCase,
    private val getRecipesUseCase: GetPopularRecipesUseCase,
    private val saveRecipeUseCase: SaveRecipeUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase,
) : ViewModel() {

    private val _recipesState =
        BehaviorSubject.createDefault<DataResult<List<Recipe>>>(DataResult.Loading)
    val recipesState: Observable<DataResult<List<Recipe>>> = _recipesState.hide()

    private val _trendingRecipesState =
        BehaviorSubject.createDefault<DataResult<List<Result>>>(DataResult.Loading)
    val trendingRecipesState: Observable<DataResult<List<Result>>> = _trendingRecipesState.hide()

    private val compositeDisposable = CompositeDisposable()

    init {
        loadRecipes()
        loadTrendingRecipes()
    }

    private fun loadTrendingRecipes() {
        compositeDisposable.add(
            getRecipesUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> _trendingRecipesState.onNext(result) },
                    { error -> _trendingRecipesState.onNext(DataResult.Error(error)) }
                )
        )
    }

    private fun loadRecipes() {
        compositeDisposable.add(
            getRandomRecipesUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> _recipesState.onNext(result) },
                    { error -> _recipesState.onNext(DataResult.Error(error)) }
                )
        )
    }

    fun saveRecipe(recipe: Recipe) {
        compositeDisposable.add(
            Completable.fromAction {
                runBlocking {
                    saveRecipeUseCase(recipe)
                }
            }
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
    }

    fun deleteRecipe(id: Int) {
        compositeDisposable.add(
            Completable.fromAction { runBlocking { deleteRecipeUseCase(id) } }
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
    }

    fun refreshRecipes() {
        loadRecipes()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}