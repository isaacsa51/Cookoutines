package com.serranoie.android.feature.instructions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.core.domain.usecase.DeleteRecipeUseCase
import com.serranoie.android.core.domain.usecase.SaveRecipeUseCase
import com.serranoie.android.feature.instructions.domain.usecase.GetRecipeByIdUseCase
import com.serranoie.android.feature.saved.domain.usecases.GetSavedRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InstructionsRecipeViewModel @Inject constructor(
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val getSavedRecipesUseCase: GetSavedRecipesUseCase,
    private val saveRecipeUseCase: SaveRecipeUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase
) : ViewModel() {
    private val _recipeState = MutableStateFlow<DataResult<Recipe>>(DataResult.Loading)
    val recipeState: StateFlow<DataResult<Recipe>> = _recipeState

    fun saveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            saveRecipeUseCase(recipe.copy(isSaved = true))
        }
    }

    fun deleteRecipe(recipeId: Int) {
        viewModelScope.launch {
            deleteRecipeUseCase(recipeId)
        }
    }

    suspend fun isRecipeSaved(recipeId: Int): Boolean {
        val savedRecipesResult = getSavedRecipesUseCase()

        return when (savedRecipesResult) {
            is DataResult.Success -> {
                savedRecipesResult.data.any { it.id == recipeId && it.isSaved == true }
            }

            else -> false
        }
    }

    fun getCurrentRecipe(id: Int) {
        viewModelScope.launch {
            _recipeState.value = DataResult.Loading
            try {
                val result = withContext(Dispatchers.IO) {
                    getRecipeByIdUseCase(id)
                }
                _recipeState.value = result
                Log.d("InstructionsRecipeViewModel", "Data: $result")
            } catch (e: Exception) {
                _recipeState.value = DataResult.Error(e)
                Log.e("InstructionsRecipeViewModel", "Error: $e")

            }
        }
    }
}