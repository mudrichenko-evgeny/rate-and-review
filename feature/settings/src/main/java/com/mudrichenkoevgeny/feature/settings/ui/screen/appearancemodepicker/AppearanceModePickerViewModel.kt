package com.mudrichenkoevgeny.feature.settings.ui.screen.appearancemodepicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mudrichenkoevgeny.core.common.enums.AppearanceMode
import com.mudrichenkoevgeny.feature.settings.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppearanceModePickerViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<AppearanceModePickerUiState> = settingsRepository.appearanceModeFlow
        .map { appearanceMode ->
            AppearanceModePickerUiState(
                pickedAppearanceMode = appearanceMode,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppearanceModePickerUiState()
        )

    fun onAppearanceModePicked(appearanceMode: AppearanceMode) {
        viewModelScope.launch {
            settingsRepository.setAppearanceMode(appearanceMode)
        }
    }
}