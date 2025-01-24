package com.serranoie.android.feature.saved

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.core.domain.model.recipe.Recipe
import com.serranoie.android.core.domain.repository.SpoonacularRepository
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.core.domain.usecase.DeleteRecipeUseCase
import com.serranoie.android.feature.saved.domain.usecases.GetSavedRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SavedRecipesViewModel @Inject constructor(
    application: Application,
    private val getSavedRecipesUseCase: GetSavedRecipesUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase,
    private val repository: SpoonacularRepository
) : AndroidViewModel(application) {

    private val _recipesState =
        MutableStateFlow<DataResult<List<Recipe>>>(DataResult.Loading)
    val recipesState: StateFlow<DataResult<List<Recipe>>> =
        _recipesState
    private val _refreshTrigger = MutableSharedFlow<Unit>()
    val refreshTrigger: SharedFlow<Unit> = _refreshTrigger.asSharedFlow()

    fun triggerRefresh() {
        viewModelScope.launch {
            _refreshTrigger.emit(Unit)
        }
    }

    init {
        loadSavedRecipes()
    }

    fun loadSavedRecipes() {
        viewModelScope.launch {
            _recipesState.value = DataResult.Loading

            val result = withContext(Dispatchers.IO) {
                getSavedRecipesUseCase()
            }

            _recipesState.value = result
        }
    }

    suspend fun deleteRecipe(id: Int) {
        viewModelScope.launch {
            deleteRecipeUseCase(id)
            _recipesState.value = repository.getSavedRecipesByDate()
        }
    }
}