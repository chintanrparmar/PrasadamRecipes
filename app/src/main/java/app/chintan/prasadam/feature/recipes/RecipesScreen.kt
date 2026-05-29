package app.chintan.prasadam.feature.recipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.chintan.prasadam.core.design.CardWhite
import app.chintan.prasadam.core.design.FreshGreen
import app.chintan.prasadam.core.design.LightGreenChip
import app.chintan.prasadam.core.design.LocalAppLanguage
import app.chintan.prasadam.core.design.PrasadamTheme
import app.chintan.prasadam.core.design.SecondaryText
import app.chintan.prasadam.core.localization.AppStrings
import app.chintan.prasadam.domain.model.Language
import app.chintan.prasadam.domain.model.RecipeCategory
import app.chintan.prasadam.feature.home.RecipeListItem
import app.chintan.prasadam.feature.home.RecipeSearchBar

// ── Route composable ──────────────────────────────────────────────────────────

@Composable
fun RecipesScreen(
    onRecipeClick: (Int) -> Unit,
    viewModel: RecipesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    RecipesContent(
        uiState = uiState,
        onCategorySelected = viewModel::onCategorySelected,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onClearSearch = viewModel::clearSearch,
        onRecipeClick = onRecipeClick
    )
}

// ── Stateless content ─────────────────────────────────────────────────────────

@Composable
private fun RecipesContent(
    uiState: RecipesUiState,
    onCategorySelected: (RecipeCategory) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onRecipeClick: (Int) -> Unit
) {
    val lang = LocalAppLanguage.current

    Column(modifier = Modifier.fillMaxSize()) {
        // Search bar
        RecipeSearchBar(
            query = uiState.searchQuery,
            onQueryChange = onSearchQueryChange,
            onClear = onClearSearch,
            placeholder = AppStrings.search(lang),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        )

        // Category chips
        if (uiState.searchQuery.isBlank()) {
            CategoryChipsRow(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = onCategorySelected,
                lang = lang
            )
        }

        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = FreshGreen)
                }
            }

            uiState.recipes.isEmpty() -> {
                EmptyRecipesState(lang = lang)
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.recipes, key = { it.id }) { recipe ->
                        RecipeListItem(
                            recipe = recipe,
                            lang = lang,
                            onClick = { onRecipeClick(recipe.id) },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryChipsRow(
    selectedCategory: RecipeCategory,
    onCategorySelected: (RecipeCategory) -> Unit,
    lang: Language
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(RecipeCategory.entries) { category ->
            FilterChip(
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                label = { Text(category.displayName(lang)) },
                colors = FilterChipDefaults.filterChipColors(
                    // Selected: light green background + dark green text
                    selectedContainerColor = LightGreenChip,
                    selectedLabelColor = FreshGreen,
                    // Unselected: white background + muted text
                    containerColor = CardWhite,
                    labelColor = SecondaryText
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = category == selectedCategory,
                    borderColor = MaterialTheme.colorScheme.outlineVariant,
                    selectedBorderColor = FreshGreen.copy(alpha = 0.3f),
                    borderWidth = 1.dp,
                    selectedBorderWidth = 1.dp
                )
            )
        }
    }
}

@Composable
private fun EmptyRecipesState(lang: Language) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🍽️", style = MaterialTheme.typography.displayMedium)
            Text(
                text = AppStrings.noResultsTitle(lang),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = AppStrings.noResultsSubtitle(lang),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp)
            )
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun CategoryChipsPreview() {
    PrasadamTheme {
        CategoryChipsRow(
            selectedCategory = RecipeCategory.ALL,
            onCategorySelected = {},
            lang = Language.ENGLISH
        )
    }
}
