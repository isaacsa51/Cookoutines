package com.serranoie.android.feature.instructions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.instructions.domain.usecase.GetRecipeByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InstructionsRecipeViewModel @Inject constructor(
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase
) : ViewModel() {
    private val _recipeState = MutableStateFlow<DataResult<Recipe>>(DataResult.Loading)
    val recipeState: StateFlow<DataResult<Recipe>> = _recipeState

    fun getCurrentRecipe(id: Int) {
        viewModelScope.launch {
            _recipeState.value = DataResult.Loading
            try {
                val result = withContext(Dispatchers.IO) {
                    getRecipeByIdUseCase(id)
                }
                _recipeState.value = result
            } catch (e: Exception) {
                _recipeState.value = DataResult.Error(e)
            }
        }
    }

    fun getRecipeInstructions(id: Int) {
        // TODO: Add endpoint and repository impl...
    }
}