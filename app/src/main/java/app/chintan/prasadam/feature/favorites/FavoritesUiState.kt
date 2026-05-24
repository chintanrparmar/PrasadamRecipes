package app.chintan.prasadam.feature.favorites

import app.chintan.prasadam.domain.model.Recipe

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState
    data object Empty : FavoritesUiState
    data class Success(val favorites: List<Recipe>) : FavoritesUiState
}
