package com.mudrichenkoevgeny.core.storage.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.mudrichenkoevgeny.core.storage.datastore.AppPreferences
import com.mudrichenkoevgeny.core.storage.datastore.AppPreferencesSerializer
import com.mudrichenkoevgeny.core.storage.repository.files.FilesRepository
import com.mudrichenkoevgeny.core.storage.repository.files.FilesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideAppPreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<AppPreferences> = createAppPreferencesDataStore(context)

    private fun createAppPreferencesDataStore(context: Context): DataStore<AppPreferences> {
        return DataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(AppPreferences.APP_PREFERENCES_FILE) },
            serializer = AppPreferencesSerializer
        )
    }

    @Provides
    @Singleton
    fun provideFilesRepository(
        @ApplicationContext context: Context,
    ): FilesRepository = FilesRepositoryImpl(
        context = context
    )
}