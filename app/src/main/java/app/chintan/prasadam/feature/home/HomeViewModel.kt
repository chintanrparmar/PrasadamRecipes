package app.chintan.prasadam.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.chintan.prasadam.domain.usecase.GetRecipesUseCase
import app.chintan.prasadam.domain.usecase.SearchRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getRecipesUseCase: GetRecipesUseCase,
    private val searchRecipesUseCase: SearchRecipesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val searchResults = _searchQuery.flatMapLatest { query ->
        searchRecipesUseCase(query)
    }

    private val allRecipes = getRecipesUseCase()

    val uiState: StateFlow<HomeUiState> = combine(
        allRecipes,
        _searchQuery,
        searchResults
    ) { recipes, query, results ->
        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val todayRecipe = if (recipes.isNotEmpty()) recipes[today % recipes.size] else null

        HomeUiState(
            isLoading = false,
            todayRecipe = todayRecipe,
            festivalRecipes = recipes.filter { it.isFestivalSpecial }.take(6),
            quickRecipes = recipes.filter { it.isQuick }.take(6),
            popularRecipes = recipes.filter { it.isPopular }.take(6),
            searchQuery = query,
            searchResults = results,
            isSearchActive = query.isNotBlank(),
            error = null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.update { query }
    }

    fun clearSearch() {
        _searchQuery.update { "" }
    }
}
