package com.mudrichenkoevgeny.feature.settings.repository

import com.mudrichenkoevgeny.core.common.enums.AppearanceMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val appearanceModeFlow: Flow<AppearanceMode>
    suspend fun setAppearanceMode(appearanceMode: AppearanceMode)
}