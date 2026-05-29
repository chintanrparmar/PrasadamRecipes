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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.chintan.prasadam.core.design.AppBackground
import app.chintan.prasadam.core.design.CardWhite
import app.chintan.prasadam.core.design.FavoriteGold
import app.chintan.prasadam.core.design.FreshGreen
import app.chintan.prasadam.core.design.LightGreenChip
import app.chintan.prasadam.core.design.LightPeach
import app.chintan.prasadam.core.design.LocalAppLanguage
import app.chintan.prasadam.core.design.MutedText
import app.chintan.prasadam.core.design.PeachTint
import app.chintan.prasadam.core.design.PoppinsFontFamily
import app.chintan.prasadam.core.design.PrasadamTheme
import app.chintan.prasadam.core.design.PrimaryText
import app.chintan.prasadam.core.design.SecondaryText
import app.chintan.prasadam.core.design.SoftCream
import app.chintan.prasadam.core.design.SoftLeafGreen
import app.chintan.prasadam.core.design.SoftOrange
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
            CircularProgressIndicator(color = FreshGreen)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // ── Hero card ─────────────────────────────────────────────────────────
        item {
            PrasadamHeroCard(lang = lang, modifier = Modifier.padding(16.dp))
        }

        // ── Search bar ────────────────────────────────────────────────────────
        item {
            RecipeSearchBar(
                query = uiState.searchQuery,
                onQueryChange = onSearchQueryChange,
                onClear = onClearSearch,
                placeholder = AppStrings.search(lang),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            )
        }

        // ── Search results ────────────────────────────────────────────────────
        if (uiState.isSearchActive) {
            if (uiState.searchResults.isEmpty()) {
                item { EmptySearchState(lang = lang) }
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
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
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
                    modifier = Modifier.padding(
                        start = 16.dp, end = 16.dp, top = 20.dp, bottom = 4.dp
                    )
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
                    modifier = Modifier.padding(
                        start = 16.dp, end = 16.dp, top = 20.dp, bottom = 4.dp
                    )
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
                    modifier = Modifier.padding(
                        start = 16.dp, end = 16.dp, top = 20.dp, bottom = 4.dp
                    )
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

// ── Shared design components ──────────────────────────────────────────────────

/**
 * Soft hero banner — replaces the old orange gradient header.
 * Warm cream-to-peach gradient with the app identity and quick badges.
 */
@Composable
fun PrasadamHeroCard(lang: Language, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(listOf(LightPeach, SoftCream, AppBackground))
            )
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = AppStrings.appTitle(lang),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = FreshGreen
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = AppStrings.appSubtitle(lang),
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryText
                )
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoBadge(
                        text = "120 recipes",
                        bgColor = LightGreenChip,
                        textColor = FreshGreen
                    )
                    InfoBadge(
                        text = "No onion · garlic",
                        bgColor = PeachTint,
                        textColor = SoftOrange
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Text(text = "🌿", style = MaterialTheme.typography.displaySmall)
        }
    }
}

/**
 * White card search field with soft shadow — replaces Material SearchBar.
 */
@Composable
fun RecipeSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = CardWhite,
        shadowElevation = 4.dp,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = if (query.isNotEmpty()) FreshGreen else SecondaryText,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(10.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = TextStyle(
                            fontFamily = PoppinsFontFamily,
                            fontSize = 14.sp,
                            color = MutedText
                        )
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    textStyle = TextStyle(
                        fontFamily = PoppinsFontFamily,
                        fontSize = 14.sp,
                        color = PrimaryText
                    ),
                    cursorBrush = SolidColor(FreshGreen),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (query.isNotEmpty()) {
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = onClear,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = SecondaryText,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * Small pill badge for metadata (recipe count, dietary info, etc.)
 */
@Composable
fun InfoBadge(
    text: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
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
            fontWeight = FontWeight.Bold,
            color = PrimaryText
        )
        if (showSeeAll) {
            TextButton(onClick = onSeeAll) {
                Text(
                    text = AppStrings.seeAll(lang),
                    color = FreshGreen,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Saffron-tinted time badge.
 */
@Composable
fun RecipeTimeBadge(minutes: Int, lang: Language, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(PeachTint)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = "⏱ $minutes ${AppStrings.minuteLabel(lang)}",
            style = MaterialTheme.typography.labelSmall,
            color = SoftOrange
        )
    }
}

/**
 * Light-green "Farali" tag.
 */
@Composable
fun FaraliChip(lang: Language, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(LightGreenChip)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = "🌿 ${AppStrings.farali(lang)}",
            style = MaterialTheme.typography.labelSmall,
            color = FreshGreen
        )
    }
}

// ── Section sub-composables ───────────────────────────────────────────────────

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
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            // Illustrated image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(categoryGradient(recipe)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = categoryEmoji(recipe),
                    style = MaterialTheme.typography.displayMedium
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = recipe.name.get(lang),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = FreshGreen
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = recipe.description.get(lang),
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RecipeTimeBadge(recipe.cookTimeMinutes, lang)
                    if (recipe.isFarali) FaraliChip(lang)
                    InfoBadge(
                        text = "No onion · No garlic",
                        bgColor = LightGreenChip,
                        textColor = FreshGreen
                    )
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

/**
 * Compact card for horizontal scroll rows (festival specials, quick recipes).
 */
@Composable
fun RecipeCard(
    recipe: Recipe,
    lang: Language,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(162.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(categoryGradient(recipe)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = categoryEmoji(recipe),
                    style = MaterialTheme.typography.headlineLarge
                )
                if (recipe.isFestivalSpecial) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = FavoriteGold,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(16.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = recipe.name.get(lang),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                RecipeTimeBadge(minutes = recipe.cookTimeMinutes, lang = lang)
            }
        }
    }
}

/**
 * Full-width list row for search results, popular recipes, etc.
 */
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
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(categoryGradient(recipe)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = categoryEmoji(recipe),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recipe.name.get(lang),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = recipe.description.get(lang),
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RecipeTimeBadge(recipe.cookTimeMinutes, lang)
                    if (recipe.isFarali) FaraliChip(lang)
                }
            }
            if (recipe.isFavorite) {
                Spacer(Modifier.width(8.dp))
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = null,
                    tint = FavoriteGold,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
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
            style = MaterialTheme.typography.titleMedium,
            color = PrimaryText
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = AppStrings.noResultsSubtitle(lang),
            style = MaterialTheme.typography.bodyMedium,
            color = SecondaryText
        )
    }
}

// ── Category helpers (shared with RecipeDetailScreen) ─────────────────────────

fun categoryEmoji(recipe: Recipe): String = when (recipe.category.name) {
    "FARALI"     -> "🌿"
    "SWEETS"     -> "🍮"
    "SABJI"      -> "🥦"
    "DAL"        -> "🫘"
    "ROTI_BREAD" -> "🫓"
    "RICE"       -> "🍚"
    "SNACKS"     -> "🥨"
    "SOUP"       -> "🍲"
    "DRINKS"     -> "🥤"
    "FESTIVAL"   -> "✨"
    else         -> "🍽️"
}

/** Soft, food-category-tinted gradient for image placeholder areas. */
fun categoryGradient(recipe: Recipe): Brush = when (recipe.category.name) {
    "FARALI"     -> Brush.verticalGradient(listOf(Color(0xFFEAF3E5), Color(0xFFD5E8CB)))
    "SWEETS"     -> Brush.verticalGradient(listOf(Color(0xFFFFF0E6), Color(0xFFFFE0CC)))
    "FESTIVAL"   -> Brush.verticalGradient(listOf(Color(0xFFFFF8E0), Color(0xFFFFF0C0)))
    "SABJI"      -> Brush.verticalGradient(listOf(Color(0xFFF0F7E8), Color(0xFFE0EDD5)))
    "DAL"        -> Brush.verticalGradient(listOf(Color(0xFFFFF5E6), Color(0xFFFFE8CC)))
    "ROTI_BREAD" -> Brush.verticalGradient(listOf(Color(0xFFFFF9E6), Color(0xFFFFF0CC)))
    "RICE"       -> Brush.verticalGradient(listOf(Color(0xFFF7F7F0), Color(0xFFEDEDE0)))
    "SNACKS"     -> Brush.verticalGradient(listOf(Color(0xFFFFF3E5), Color(0xFFFFE5CC)))
    "SOUP"       -> Brush.verticalGradient(listOf(Color(0xFFE8F4F8), Color(0xFFD5ECF5)))
    "DRINKS"     -> Brush.verticalGradient(listOf(Color(0xFFE8F5E9), Color(0xFFD5ECD7)))
    else         -> Brush.verticalGradient(listOf(Color(0xFFF5F5F5), Color(0xFFEAEAEA)))
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFFFF8EF)
@Composable
private fun HeroCardPreview() {
    PrasadamTheme {
        PrasadamHeroCard(lang = Language.ENGLISH, modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFF8EF)
@Composable
private fun SearchBarPreview() {
    PrasadamTheme {
        RecipeSearchBar(
            query = "",
            onQueryChange = {},
            onClear = {},
            placeholder = "Search recipes…",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}
