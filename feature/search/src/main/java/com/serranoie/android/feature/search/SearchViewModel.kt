package com.serranoie.android.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.core.domain.model.search.Result
import com.serranoie.android.core.domain.result.DataResult
import com.serranoie.android.feature.recipes_list.domain.usecase.SearchRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRecipeUseCase: SearchRecipeUseCase
) : ViewModel() {

    private val _searchResultsState =
        MutableStateFlow<DataResult<List<Result>>>(DataResult.Success(emptyList()))
    val searchResultsState: StateFlow<DataResult<List<Result>>> = _searchResultsState

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            _searchResultsState.value = DataResult.Loading
            val result = withContext(Dispatchers.IO) {
                searchRecipeUseCase(query)
            }
            _searchResultsState.value = result
        }
    }
}