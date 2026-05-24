package app.chintan.prasadam.feature.recipedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.chintan.prasadam.core.navigation.Screen
import app.chintan.prasadam.domain.usecase.GetRecipeByIdUseCase
import app.chintan.prasadam.domain.usecase.ToggleFavoriteRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteRecipeUseCase
) : ViewModel() {

    private val recipeId: Int = checkNotNull(savedStateHandle[Screen.RecipeDetail.ARG_RECIPE_ID])

    val uiState: StateFlow<RecipeDetailUiState> =
        getRecipeByIdUseCase(recipeId).map { recipe ->
            if (recipe == null) RecipeDetailUiState.NotFound
            else RecipeDetailUiState.Success(recipe)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RecipeDetailUiState.Loading
        )

    fun toggleFavorite() {
        viewModelScope.launch {
            toggleFavoriteUseCase(recipeId)
        }
    }
}
