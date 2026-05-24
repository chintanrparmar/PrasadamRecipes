package app.chintan.prasadam.feature.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.chintan.prasadam.core.design.LocalAppLanguage
import app.chintan.prasadam.core.design.PrasadamTheme
import app.chintan.prasadam.core.design.Saffron
import app.chintan.prasadam.core.localization.AppStrings
import app.chintan.prasadam.domain.model.Language
import app.chintan.prasadam.feature.home.RecipeListItem

// ── Route composable ──────────────────────────────────────────────────────────

@Composable
fun FavoritesScreen(
    onRecipeClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FavoritesContent(uiState = uiState, onRecipeClick = onRecipeClick)
}

// ── Stateless content ─────────────────────────────────────────────────────────

@Composable
private fun FavoritesContent(
    uiState: FavoritesUiState,
    onRecipeClick: (Int) -> Unit
) {
    val lang = LocalAppLanguage.current

    when (uiState) {
        is FavoritesUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Saffron)
            }
        }

        is FavoritesUiState.Empty -> {
            FavoritesEmptyState(lang = lang)
        }

        is FavoritesUiState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.favorites, key = { it.id }) { recipe ->
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

@Composable
private fun FavoritesEmptyState(lang: Language) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text("🤍", style = MaterialTheme.typography.displayLarge)
            Spacer(Modifier.height(20.dp))
            Text(
                text = AppStrings.noFavoritesTitle(lang),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = AppStrings.noFavoritesSubtitle(lang),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun FavoritesEmptyPreview() {
    PrasadamTheme {
        FavoritesEmptyState(lang = Language.ENGLISH)
    }
}
