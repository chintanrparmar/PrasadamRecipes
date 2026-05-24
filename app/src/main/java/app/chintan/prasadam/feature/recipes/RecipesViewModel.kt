package app.chintan.prasadam.feature.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.chintan.prasadam.domain.model.RecipeCategory
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
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val searchRecipesUseCase: SearchRecipesUseCase
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow(RecipeCategory.ALL)
    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val displayedRecipes = combine(_selectedCategory, _searchQuery) { cat, query ->
        Pair(cat, query)
    }.flatMapLatest { (category, query) ->
        if (query.isBlank()) {
            getRecipesUseCase(category)
        } else {
            searchRecipesUseCase(query)
        }
    }

    val uiState: StateFlow<RecipesUiState> = combine(
        displayedRecipes,
        _selectedCategory,
        _searchQuery
    ) { recipes, category, query ->
        RecipesUiState(
            isLoading = false,
            recipes = recipes,
            selectedCategory = category,
            searchQuery = query,
            error = null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RecipesUiState()
    )

    fun onCategorySelected(category: RecipeCategory) {
        _selectedCategory.update { category }
        _searchQuery.update { "" }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.update { query }
        if (query.isNotBlank()) {
            _selectedCategory.update { RecipeCategory.ALL }
        }
    }

    fun clearSearch() {
        _searchQuery.update { "" }
    }
}
