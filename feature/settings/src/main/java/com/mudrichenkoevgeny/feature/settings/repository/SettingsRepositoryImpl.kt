package com.mudrichenkoevgeny.feature.settings.repository

import androidx.datastore.core.DataStore
import com.mudrichenkoevgeny.core.common.enums.AppearanceMode
import com.mudrichenkoevgeny.core.storage.datastore.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val appPreferences: DataStore<AppPreferences>
) : SettingsRepository {

    override val appearanceModeFlow: Flow<AppearanceMode> =
        appPreferences.data.map { it.appearanceMode }

    override suspend fun setAppearanceMode(appearanceMode: AppearanceMode) {
        appPreferences.updateData { appPreferences ->
            appPreferences.copy(
                appearanceMode = appearanceMode
            )
        }
    }
}