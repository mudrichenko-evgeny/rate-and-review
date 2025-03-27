package com.mudrichenkoevgeny.feature.settings.ui.screen.appearancemodepicker

import com.mudrichenkoevgeny.core.common.enums.AppearanceMode

data class AppearanceModePickerUiState(
    val pickedAppearanceMode: AppearanceMode = AppearanceMode.default(),
    val isLoading: Boolean = true
)