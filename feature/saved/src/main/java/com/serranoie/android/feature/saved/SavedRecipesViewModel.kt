package com.serranoie.android.feature.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.saved.domain.usecases.GetSavedRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SavedRecipesViewModel @Inject constructor(
    private val getSavedRecipesUseCase: GetSavedRecipesUseCase
) : ViewModel() {

    private val _recipesState =
        MutableStateFlow<DataResult<List<Recipe>>>(DataResult.Loading)
    val recipesState: StateFlow<DataResult<List<Recipe>>> =
        _recipesState

    init {
        loadSavedRecipes()
    }

    private fun loadSavedRecipes() {
        viewModelScope.launch {
            _recipesState.value = DataResult.Loading

            val result = withContext(Dispatchers.IO) {
                getSavedRecipesUseCase()
            }

            _recipesState.value = result
        }
    }

}