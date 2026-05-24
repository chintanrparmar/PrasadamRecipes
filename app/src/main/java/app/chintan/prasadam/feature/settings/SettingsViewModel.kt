package app.chintan.prasadam.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.chintan.prasadam.domain.model.Language
import app.chintan.prasadam.domain.model.ThemePreference
import app.chintan.prasadam.domain.usecase.GetUserPreferencesUseCase
import app.chintan.prasadam.domain.usecase.UpdateLanguageUseCase
import app.chintan.prasadam.domain.usecase.UpdateThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> =
        getUserPreferencesUseCase().map { prefs ->
            SettingsUiState(isLoading = false, preferences = prefs)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState()
        )

    fun onLanguageSelected(language: Language) {
        viewModelScope.launch { updateLanguageUseCase(language) }
    }

    fun onThemeSelected(theme: ThemePreference) {
        viewModelScope.launch { updateThemeUseCase(theme) }
    }
}
