package com.serranoie.android.feature.recipes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.recipes_list.domain.usecase.GetRandomRecipesUseCase
import com.serranoie.android.feature.recipes_list.domain.usecase.SearchRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject constructor(
    private val getRecipesUseCase: GetRandomRecipesUseCase,
    private val getRandomRecipesUseCase: GetRandomRecipesUseCase,
    private val searchRecipesUseCase: SearchRecipeUseCase
) : ViewModel() {

    private val _recipesState =
        MutableStateFlow<DataResult<List<Recipe>>>(DataResult.Loading)
    val recipesState: StateFlow<DataResult<List<Recipe>>> =
        _recipesState

    private val _trendingRecipesState =
        MutableStateFlow<DataResult<List<Recipe>>>(DataResult.Loading)
    val trendingRecipesState: StateFlow<DataResult<List<Recipe>>> =
        _trendingRecipesState

    private val _searchResultsState =
        MutableStateFlow<DataResult<List<com.serranoie.android.core.domain.model.search.Result>>>(
            DataResult.Loading
        )
    val searchResultsState: StateFlow<DataResult<List<com.serranoie.android.core.domain.model.search.Result>>> =
        _searchResultsState

    init {
        loadRecipes()
        loadTrendingRecipes()
    }

    private fun loadTrendingRecipes() {
        viewModelScope.launch {
            _trendingRecipesState.value = DataResult.Loading
            val result = withContext(Dispatchers.IO) {
                getRandomRecipesUseCase()
            }
            _trendingRecipesState.value = result
        }
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            _recipesState.value = DataResult.Loading

            val result = withContext(Dispatchers.IO) {
                getRecipesUseCase()
            }
            _recipesState.value = result
        }
    }

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            _searchResultsState.value = DataResult.Loading
            val result = withContext(Dispatchers.IO) {
                searchRecipesUseCase(query)
            }
            _searchResultsState.value = result
        }
    }

    fun resetToRandomRecipes() {
        viewModelScope.launch {
            _recipesState.value = DataResult.Loading
            val result = withContext(Dispatchers.IO) {
                getRecipesUseCase()
            }
            _recipesState.value = result
        }
    }
}