package app.chintan.prasadam.feature.recipedetail

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.chintan.prasadam.core.design.CardWhite
import app.chintan.prasadam.core.design.DarkForestGreen
import app.chintan.prasadam.core.design.FavoriteGold
import app.chintan.prasadam.core.design.FreshGreen
import app.chintan.prasadam.core.design.LightGreenChip
import app.chintan.prasadam.core.design.LightPeach
import app.chintan.prasadam.core.design.LocalAppLanguage
import app.chintan.prasadam.core.design.PeachTint
import app.chintan.prasadam.core.design.PrimaryText
import app.chintan.prasadam.core.design.SecondaryText
import app.chintan.prasadam.core.design.SoftLeafGreen
import app.chintan.prasadam.core.design.SoftOrange
import app.chintan.prasadam.core.design.WarmSaffron
import app.chintan.prasadam.core.localization.AppStrings
import app.chintan.prasadam.domain.model.Language
import app.chintan.prasadam.domain.model.Recipe
import app.chintan.prasadam.feature.home.FaraliChip
import app.chintan.prasadam.feature.home.categoryEmoji
import app.chintan.prasadam.feature.home.categoryGradient

// ── Route composable ──────────────────────────────────────────────────────────

@Composable
fun RecipeDetailScreen(
    onBackClick: () -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is RecipeDetailUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = FreshGreen)
            }
        }

        is RecipeDetailUiState.Success -> {
            RecipeDetailContent(
                recipe = state.recipe,
                onBackClick = onBackClick,
                onToggleFavorite = viewModel::toggleFavorite
            )
        }

        is RecipeDetailUiState.NotFound, is RecipeDetailUiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Recipe not found 😔", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

// ── Stateless content ─────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun RecipeDetailContent(
    recipe: Recipe,
    onBackClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val lang = LocalAppLanguage.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ── Hero image area ───────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(DarkForestGreen, FreshGreen, SoftLeafGreen)
                    )
                )
        ) {
            // Category-specific emoji, large and centered
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = categoryEmoji(recipe),
                    style = MaterialTheme.typography.displayLarge
                )
            }

            // Semi-transparent top bar overlay
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.30f))
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.30f))
                    ) {
                        Icon(
                            imageVector = if (recipe.isFavorite) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (recipe.isFavorite) FavoriteGold else Color.White
                        )
                    }
                    IconButton(
                        onClick = {
                            context.startActivity(
                                Intent.createChooser(
                                    Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, buildShareText(recipe, lang))
                                    },
                                    AppStrings.share(lang)
                                )
                            )
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.30f))
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }

        // ── White content sheet with rounded top corners ──────────────────────
        Surface(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = CardWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {

                // Badges row
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CategoryChip(text = recipe.category.displayName(lang))
                    if (recipe.isFarali) FaraliChip(lang = lang)
                    if (recipe.isFestivalSpecial) FestivalChip(lang = lang)
                }

                Spacer(Modifier.height(14.dp))

                // Recipe title — dark green for premium feel
                Text(
                    text = recipe.name.get(lang),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = FreshGreen
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = recipe.description.get(lang),
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryText
                )

                Spacer(Modifier.height(20.dp))

                // ── Meta info mini-cards ───────────────────────────────────────
                MetaInfoGrid(recipe = recipe, lang = lang)

                Spacer(Modifier.height(28.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(Modifier.height(24.dp))

                // ── Ingredients ───────────────────────────────────────────────
                SectionLabel(text = AppStrings.ingredients(lang))
                Spacer(Modifier.height(12.dp))
                recipe.ingredients.get(lang).forEach { ingredient ->
                    IngredientRow(text = ingredient)
                }

                Spacer(Modifier.height(28.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(Modifier.height(24.dp))

                // ── Instructions ──────────────────────────────────────────────
                SectionLabel(text = AppStrings.instructions(lang))
                Spacer(Modifier.height(12.dp))
                recipe.instructions.get(lang).forEachIndexed { index, step ->
                    InstructionStep(number = index + 1, text = step)
                    Spacer(Modifier.height(14.dp))
                }

                // ── Notes ─────────────────────────────────────────────────────
                recipe.notes?.let { notes ->
                    val noteText = notes.get(lang)
                    if (noteText.isNotBlank()) {
                        Spacer(Modifier.height(4.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(Modifier.height(24.dp))
                        NoteCard(title = AppStrings.notes(lang), body = noteText)
                    }
                }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

// ── Section helpers ───────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(FreshGreen)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryText
        )
    }
}

@Composable
private fun MetaInfoGrid(recipe: Recipe, lang: Language) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MetaCell(
            modifier = Modifier.weight(1f),
            emoji = "⏱",
            label = AppStrings.prepTime(lang),
            value = "${recipe.prepTimeMinutes} ${AppStrings.minuteLabel(lang)}"
        )
        MetaCell(
            modifier = Modifier.weight(1f),
            emoji = "🔥",
            label = AppStrings.cookTime(lang),
            value = "${recipe.cookTimeMinutes} ${AppStrings.minuteLabel(lang)}"
        )
        MetaCell(
            modifier = Modifier.weight(1f),
            emoji = "👥",
            label = AppStrings.servings(lang),
            value = "${recipe.servings}"
        )
        MetaCell(
            modifier = Modifier.weight(1f),
            emoji = "⭐",
            label = AppStrings.difficulty(lang),
            value = recipe.difficulty.label(lang)
        )
    }
}

@Composable
private fun MetaCell(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(LightGreenChip)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = emoji, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = FreshGreen
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = SecondaryText
        )
    }
}

@Composable
private fun IngredientRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(FreshGreen)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = PrimaryText
        )
    }
}

@Composable
private fun InstructionStep(number: Int, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Green circle with white step number
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(FreshGreen),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = PrimaryText,
            modifier = Modifier
                .weight(1f)
                .padding(top = 4.dp)
        )
    }
}

@Composable
private fun NoteCard(title: String, body: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightPeach.copy(alpha = 0.45f)),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "💡", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = SoftOrange
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = PrimaryText
            )
        }
    }
}

@Composable
private fun CategoryChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(LightGreenChip)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = FreshGreen,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun FestivalChip(lang: Language) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(PeachTint)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = "✨ ${AppStrings.festivalSpecial(lang)}",
            style = MaterialTheme.typography.labelSmall,
            color = WarmSaffron,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun buildShareText(recipe: Recipe, lang: Language): String = buildString {
    appendLine(recipe.name.get(lang))
    appendLine()
    appendLine(recipe.description.get(lang))
    appendLine()
    appendLine("Prasadam Recipes — Sattvik cooking app")
}
