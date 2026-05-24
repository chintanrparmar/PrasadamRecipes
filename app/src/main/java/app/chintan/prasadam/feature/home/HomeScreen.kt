package app.chintan.prasadam.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.chintan.prasadam.core.design.DeepSaffron
import app.chintan.prasadam.core.design.LocalAppLanguage
import app.chintan.prasadam.core.design.PrasadamTheme
import app.chintan.prasadam.core.design.Saffron
import app.chintan.prasadam.core.design.TempleGold
import app.chintan.prasadam.core.localization.AppStrings
import app.chintan.prasadam.domain.model.Language
import app.chintan.prasadam.domain.model.Recipe

// ── Route composable ──────────────────────────────────────────────────────────

@Composable
fun HomeScreen(
    onRecipeClick: (Int) -> Unit,
    onSeeAllClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onClearSearch = viewModel::clearSearch,
        onRecipeClick = onRecipeClick,
        onSeeAllClick = onSeeAllClick
    )
}

// ── Stateless content ─────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onRecipeClick: (Int) -> Unit,
    onSeeAllClick: () -> Unit
) {
    val lang = LocalAppLanguage.current

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Saffron)
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        item {
            HomeHeader(lang = lang)
        }

        // ── Search bar ────────────────────────────────────────────────────────
        item {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = uiState.searchQuery,
                        onQueryChange = onSearchQueryChange,
                        onSearch = {},
                        expanded = false,
                        onExpandedChange = {},
                        placeholder = { Text(AppStrings.search(lang)) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (uiState.isSearchActive) {
                                IconButton(onClick = onClearSearch) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        }
                    )
                },
                expanded = false,
                onExpandedChange = {}
            ) {}
        }

        // ── Search results ────────────────────────────────────────────────────
        if (uiState.isSearchActive) {
            if (uiState.searchResults.isEmpty()) {
                item {
                    EmptySearchState(lang = lang)
                }
            } else {
                items(uiState.searchResults, key = { it.id }) { recipe ->
                    RecipeListItem(
                        recipe = recipe,
                        lang = lang,
                        onClick = { onRecipeClick(recipe.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
            return@LazyColumn
        }

        // ── Today's Recipe ────────────────────────────────────────────────────
        uiState.todayRecipe?.let { recipe ->
            item {
                SectionHeader(
                    title = AppStrings.todaysRecipe(lang),
                    showSeeAll = false,
                    onSeeAll = {},
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                TodayRecipeCard(
                    recipe = recipe,
                    lang = lang,
                    onClick = { onRecipeClick(recipe.id) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        // ── Festival Specials ─────────────────────────────────────────────────
        if (uiState.festivalRecipes.isNotEmpty()) {
            item {
                SectionHeader(
                    title = AppStrings.festivalSpecials(lang),
                    showSeeAll = true,
                    onSeeAll = onSeeAllClick,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                HorizontalRecipeRow(
                    recipes = uiState.festivalRecipes,
                    lang = lang,
                    onRecipeClick = onRecipeClick
                )
            }
        }

        // ── Quick Recipes ─────────────────────────────────────────────────────
        if (uiState.quickRecipes.isNotEmpty()) {
            item {
                SectionHeader(
                    title = AppStrings.quickRecipes(lang),
                    showSeeAll = true,
                    onSeeAll = onSeeAllClick,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                HorizontalRecipeRow(
                    recipes = uiState.quickRecipes,
                    lang = lang,
                    onRecipeClick = onRecipeClick
                )
            }
        }

        // ── Popular Recipes ───────────────────────────────────────────────────
        if (uiState.popularRecipes.isNotEmpty()) {
            item {
                SectionHeader(
                    title = AppStrings.popularRecipes(lang),
                    showSeeAll = true,
                    onSeeAll = onSeeAllClick,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            items(uiState.popularRecipes, key = { it.id }) { recipe ->
                RecipeListItem(
                    recipe = recipe,
                    lang = lang,
                    onClick = { onRecipeClick(recipe.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// ── Sub-components ────────────────────────────────────────────────────────────

@Composable
private fun HomeHeader(lang: Language, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(listOf(DeepSaffron, Saffron))
            )
            .padding(horizontal = 20.dp, vertical = 28.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🪔",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = AppStrings.appTitle(lang),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = AppStrings.appSubtitle(lang),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    showSeeAll: Boolean,
    onSeeAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (showSeeAll) {
            TextButton(onClick = onSeeAll) {
                Text(
                    text = AppStrings.seeAll(lang),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun TodayRecipeCard(
    recipe: Recipe,
    lang: Language,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder illustration
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.radialGradient(listOf(TempleGold, DeepSaffron))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🍽️", style = MaterialTheme.typography.headlineMedium)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recipe.name.get(lang),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = recipe.description.get(lang),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                Row {
                    RecipeTimeBadge(
                        minutes = recipe.cookTimeMinutes,
                        lang = lang
                    )
                    if (recipe.isFarali) {
                        Spacer(Modifier.width(6.dp))
                        FaraliChip(lang = lang)
                    }
                }
            }
        }
    }
}

@Composable
private fun HorizontalRecipeRow(
    recipes: List<Recipe>,
    lang: Language,
    onRecipeClick: (Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(recipes, key = { it.id }) { recipe ->
            RecipeCard(
                recipe = recipe,
                lang = lang,
                onClick = { onRecipeClick(recipe.id) }
            )
        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    lang: Language,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Saffron.copy(alpha = 0.6f),
                                DeepSaffron.copy(alpha = 0.8f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = categoryEmoji(recipe), style = MaterialTheme.typography.headlineSmall)
                if (recipe.isFestivalSpecial) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = TempleGold,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(16.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = recipe.name.get(lang),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                RecipeTimeBadge(minutes = recipe.cookTimeMinutes, lang = lang)
            }
        }
    }
}

@Composable
fun RecipeListItem(
    recipe: Recipe,
    lang: Language,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.radialGradient(listOf(Saffron.copy(0.4f), DeepSaffron.copy(0.6f)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = categoryEmoji(recipe), style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recipe.name.get(lang),
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = recipe.description.get(lang),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                RecipeTimeBadge(minutes = recipe.cookTimeMinutes, lang = lang)
            }
        }
    }
}

@Composable
fun RecipeTimeBadge(minutes: Int, lang: Language, modifier: Modifier = Modifier) {
    Text(
        text = "⏱ $minutes ${AppStrings.minuteLabel(lang)}",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}

@Composable
fun FaraliChip(lang: Language, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "🌿 ${AppStrings.farali(lang)}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}

@Composable
private fun EmptySearchState(lang: Language) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🔍", style = MaterialTheme.typography.displaySmall)
        Spacer(Modifier.height(16.dp))
        Text(
            text = AppStrings.noResultsTitle(lang),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = AppStrings.noResultsSubtitle(lang),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun categoryEmoji(recipe: Recipe): String = when (recipe.category.name) {
    "FARALI" -> "🌿"
    "SWEETS" -> "🍮"
    "SABJI" -> "🥦"
    "DAL" -> "🫘"
    "ROTI_BREAD" -> "🫓"
    "RICE" -> "🍚"
    "SNACKS" -> "🥨"
    "SOUP" -> "🍲"
    "DRINKS" -> "🥤"
    "FESTIVAL" -> "🪔"
    else -> "🍽️"
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun HomeHeaderPreview() {
    PrasadamTheme {
        HomeHeader(lang = Language.ENGLISH)
    }
}
