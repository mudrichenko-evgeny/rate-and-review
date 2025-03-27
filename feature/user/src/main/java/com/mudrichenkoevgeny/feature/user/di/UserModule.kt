package com.mudrichenkoevgeny.feature.user.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.mudrichenkoevgeny.feature.user.datastore.UserPreferences
import com.mudrichenkoevgeny.feature.user.datastore.UserPreferencesSerializer
import com.mudrichenkoevgeny.feature.user.network.restapi.auth.AuthRestApi
import com.mudrichenkoevgeny.feature.user.network.restapi.auth.MockAuthRestApi
import com.mudrichenkoevgeny.feature.user.repository.UserRepository
import com.mudrichenkoevgeny.feature.user.repository.UserRepositoryImpl
import com.mudrichenkoevgeny.core.common.di.IsMockRestApi
import com.mudrichenkoevgeny.core.network.repository.NetworkRepository
import com.mudrichenkoevgeny.feature.user.network.restapi.user.UserRestApi
import com.mudrichenkoevgeny.feature.user.network.restapi.user.MockUserRestApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideAuthPreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<UserPreferences> = createAuthPreferencesDataStore(context)

    private fun createAuthPreferencesDataStore(context: Context): DataStore<UserPreferences> {
        return DataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(UserPreferences.Companion.USER_PREFERENCES_FILE) },
            serializer = UserPreferencesSerializer,
        )
    }

    @Provides
    @Singleton
    fun provideAuthRestApi(
        @IsMockRestApi isMockRestApi: Boolean,
        retrofit: Retrofit
    ): AuthRestApi {
        return if (isMockRestApi) {
            MockAuthRestApi()
        } else {
            retrofit.create(AuthRestApi::class.java)
        }
    }

    @Provides
    @Singleton
    fun provideUserRestApi(
        @IsMockRestApi isMockRestApi: Boolean,
        retrofit: Retrofit
    ): UserRestApi {
        return if (isMockRestApi) {
            MockUserRestApi()
        } else {
            retrofit.create(UserRestApi::class.java)
        }
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        networkRepository: NetworkRepository,
        userPreferences: DataStore<UserPreferences>,
        authRestApi: AuthRestApi,
        userRestApi: UserRestApi
    ): UserRepository {
        return UserRepositoryImpl(
            networkRepository,
            userPreferences,
            authRestApi,
            userRestApi
        )
    }
}