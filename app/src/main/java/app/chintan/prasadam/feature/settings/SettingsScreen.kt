package app.chintan.prasadam.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.chintan.prasadam.core.design.LocalAppLanguage
import app.chintan.prasadam.core.design.PrasadamTheme
import app.chintan.prasadam.core.design.Saffron
import app.chintan.prasadam.core.localization.AppStrings
import app.chintan.prasadam.domain.model.Language
import app.chintan.prasadam.domain.model.ThemePreference
import app.chintan.prasadam.domain.model.UserPreferences

// ── Route composable ──────────────────────────────────────────────────────────

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (!uiState.isLoading) {
        SettingsContent(
            uiState = uiState,
            onLanguageSelected = viewModel::onLanguageSelected,
            onThemeSelected = viewModel::onThemeSelected
        )
    }
}

// ── Stateless content ─────────────────────────────────────────────────────────

@Composable
private fun SettingsContent(
    uiState: SettingsUiState,
    onLanguageSelected: (Language) -> Unit,
    onThemeSelected: (ThemePreference) -> Unit
) {
    val lang = LocalAppLanguage.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // ── Language ──────────────────────────────────────────────────────────
        SettingsSectionTitle(text = AppStrings.languageTitle(lang))
        Spacer(Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.selectableGroup()) {
                Language.entries.forEachIndexed { index, language ->
                    RadioRow(
                        label = "${language.nativeName} (${language.displayName})",
                        selected = uiState.selectedLanguage == language,
                        onSelect = { onLanguageSelected(language) }
                    )
                    if (index < Language.entries.lastIndex) HorizontalDivider(Modifier.padding(start = 56.dp))
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── Theme ─────────────────────────────────────────────────────────────
        SettingsSectionTitle(text = AppStrings.themeTitle(lang))
        Spacer(Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            val themes = listOf(
                ThemePreference.LIGHT to AppStrings.themeLight(lang),
                ThemePreference.DARK to AppStrings.themeDark(lang),
                ThemePreference.SYSTEM to AppStrings.themeSystem(lang)
            )
            Column(Modifier.selectableGroup()) {
                themes.forEachIndexed { index, (theme, label) ->
                    RadioRow(
                        label = label,
                        selected = uiState.selectedTheme == theme,
                        onSelect = { onThemeSelected(theme) }
                    )
                    if (index < themes.lastIndex) HorizontalDivider(Modifier.padding(start = 56.dp))
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        // ── About ─────────────────────────────────────────────────────────────
        SettingsSectionTitle(text = AppStrings.about(lang))
        Spacer(Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🪔", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = AppStrings.appTitle(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${AppStrings.version(lang)} 1.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(12.dp))
                Text(
                    text = AppStrings.disclaimer(lang),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic
                )
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun SettingsSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = Saffron,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
private fun RadioRow(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onSelect,
                role = Role.RadioButton
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors = RadioButtonDefaults.colors(selectedColor = Saffron)
        )
        Spacer(Modifier.width(12.dp))
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun SettingsPreview() {
    PrasadamTheme {
        SettingsContent(
            uiState = SettingsUiState(
                isLoading = false,
                preferences = UserPreferences()
            ),
            onLanguageSelected = {},
            onThemeSelected = {}
        )
    }
}
