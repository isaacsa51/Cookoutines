package com.serranoie.android.feature.search

import androidx.lifecycle.ViewModel
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.recipes_list.domain.usecase.SearchRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRecipeUseCase: SearchRecipeUseCase
) : ViewModel() {

    private val _searchResultsState =
        BehaviorSubject.createDefault<DataResult<List<Result>>>(DataResult.Loading)
    val searchResultsState: Observable<DataResult<List<Result>>> = _searchResultsState.hide()

    private val _recipesState =
        BehaviorSubject.createDefault<DataResult<List<Recipe>>>(DataResult.Loading)
    val recipesState: Observable<DataResult<List<Recipe>>> = _recipesState.hide()

    private val compositeDisposable = CompositeDisposable()

    fun searchRecipes(query: String) {
        compositeDisposable.add(
            searchRecipeUseCase(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> _searchResultsState.onNext(result) },
                    { error -> _searchResultsState.onNext(DataResult.Error(error)) }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}