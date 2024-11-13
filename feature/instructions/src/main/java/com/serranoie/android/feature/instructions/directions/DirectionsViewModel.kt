package com.serranoie.android.feature.instructions.directions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.core.domain.model.instructions.InstructionsItem
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.instructions.domain.usecase.GetDetailedInstructionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DirectionsViewModel @Inject constructor(
    private val getDetailedInstructionsUseCase: GetDetailedInstructionsUseCase
) : ViewModel() {

    private val _instructions =
        MutableStateFlow<DataResult<List<InstructionsItem>>>(DataResult.Loading)
    val instructions: StateFlow<DataResult<List<InstructionsItem>>> = _instructions

    fun getRecipeInstructions(recipeId: Int) {
        viewModelScope.launch {
            _instructions.value = DataResult.Loading

            try {
                val result = withContext(Dispatchers.IO) {
                    getDetailedInstructionsUseCase(recipeId)
                }

                _instructions.value = result

            } catch (e: Exception) {
                _instructions.value = DataResult.Error(e)
            }
        }
    }
}