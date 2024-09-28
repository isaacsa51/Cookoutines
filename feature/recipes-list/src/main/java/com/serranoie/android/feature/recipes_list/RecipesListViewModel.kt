package com.serranoie.android.feature.recipes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.feature.recipes_list.domain.usecase.GetRandomRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject constructor(
    private val getRecipesUseCase: GetRandomRecipesUseCase
) : ViewModel() {

    private val _recipesState =
        MutableStateFlow<com.serranoie.android.core.domain.result.Result<List<Recipe>>>(com.serranoie.android.core.domain.result.Result.Loading)
    val recipesState: StateFlow<com.serranoie.android.core.domain.result.Result<List<Recipe>>> =
        _recipesState

    init {
        loadRecipes()
    }

    private fun loadRecipes() {


        viewModelScope.launch {
            _recipesState.value = com.serranoie.android.core.domain.result.Result.Loading

            val result = withContext(Dispatchers.IO) {
                getRecipesUseCase()
            }

            _recipesState.value = result
        }
    }
}