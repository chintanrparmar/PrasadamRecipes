package app.chintan.prasadam.feature.recipedetail

import app.chintan.prasadam.domain.model.Recipe

sealed interface RecipeDetailUiState {
    data object Loading : RecipeDetailUiState
    data class Success(val recipe: Recipe) : RecipeDetailUiState
    data object NotFound : RecipeDetailUiState
    data class Error(val message: String) : RecipeDetailUiState
}
