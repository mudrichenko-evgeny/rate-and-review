package com.mudrichenkoevgeny.feature.settings.di

import androidx.datastore.core.DataStore
import com.mudrichenkoevgeny.core.storage.datastore.AppPreferences
import com.mudrichenkoevgeny.feature.settings.repository.SettingsRepository
import com.mudrichenkoevgeny.feature.settings.repository.SettingsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Provides
    @Singleton
    fun provideSettingsRepository(
        appPreferences: DataStore<AppPreferences>
    ): SettingsRepository {
        return SettingsRepositoryImpl(
            appPreferences = appPreferences
        )
    }
}