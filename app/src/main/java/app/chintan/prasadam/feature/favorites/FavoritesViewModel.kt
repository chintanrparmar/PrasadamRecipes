package app.chintan.prasadam.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.chintan.prasadam.domain.usecase.GetFavoriteRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> =
        getFavoriteRecipesUseCase().map { favorites ->
            if (favorites.isEmpty()) FavoritesUiState.Empty
            else FavoritesUiState.Success(favorites)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoritesUiState.Loading
        )
}
