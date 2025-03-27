package com.mudrichenkoevgeny.core.network.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.mudrichenkoevgeny.core.common.di.BaseApiUrl
import com.mudrichenkoevgeny.core.network.datastore.NetworkSessionData
import com.mudrichenkoevgeny.core.network.datastore.NetworkSessionDataSerializer
import com.mudrichenkoevgeny.core.network.okhttp.OkHttpAuthenticator
import com.mudrichenkoevgeny.core.network.okhttp.OkHttpInterceptor
import com.mudrichenkoevgeny.core.network.repository.NetworkRepository
import com.mudrichenkoevgeny.core.network.repository.NetworkRepositoryImpl
import com.mudrichenkoevgeny.core.network.retrofit.NetworkResultCallAdapterFactory
import com.mudrichenkoevgeny.core.network.util.NetworkChecker
import com.mudrichenkoevgeny.core.network.util.NetworkCheckerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    const val DEFAULT_BASE_API_URL = "https://example.com/"
    const val RETROFIT_CONVERTER_CONTENT_TYPE = "application/json"

    @Provides
    @Singleton
    fun provideNetworkSessionDataStore(
        @ApplicationContext context: Context
    ): DataStore<NetworkSessionData> = createNetworkSessionDataStore(context)

    private fun createNetworkSessionDataStore(context: Context): DataStore<NetworkSessionData> {
        return DataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(NetworkSessionData.Companion.NETWORK_SESSION_PREFERENCES_FILE) },
            serializer = NetworkSessionDataSerializer,
        )
    }

    @Provides
    @Singleton
    fun provideNetworkChecker(
        @ApplicationContext context: Context
    ): NetworkChecker {
        return NetworkCheckerImpl(
            context
        )
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(
        networkSessionDataStore: DataStore<NetworkSessionData>
    ): NetworkRepository {
        return NetworkRepositoryImpl(
            networkSessionDataStore
        )
    }

    @DefaultOkHttpInterceptor
    @Provides
    @Singleton
    fun provideOkHttpInterceptor(
        networkRepository: NetworkRepository,
        networkChecker: NetworkChecker
    ): Interceptor {
        return OkHttpInterceptor(
            networkRepository,
            networkChecker
        )
    }

    @LoggingOkHttpInterceptor
    @Provides
    @Singleton
    fun provideOkHttpLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @TokenRefreshOkHttpClient
    @Provides
    @Singleton
    fun provideRefreshTokenOkHttpClient(
        @LoggingOkHttpInterceptor loggingInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpAuthenticator(
        @BaseApiUrl baseApiUrl: String = DEFAULT_BASE_API_URL,
        @TokenRefreshOkHttpClient tokenRefreshOkHttpClient: OkHttpClient,
        networkRepository: NetworkRepository
    ): OkHttpAuthenticator {
        return OkHttpAuthenticator(
            tokenRefreshOkHttpClient,
            networkRepository,
            baseApiUrl
        )
    }

    @DefaultOkHttpClient
    @Provides
    @Singleton
    fun provideOkHttpClient(
        @DefaultOkHttpInterceptor defaultOkHttpInterceptor: Interceptor,
        @LoggingOkHttpInterceptor loggingOkHttpInterceptor: Interceptor,
        okHttpAuthenticator: OkHttpAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingOkHttpInterceptor)
            .addInterceptor(defaultOkHttpInterceptor)
            .authenticator(okHttpAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @BaseApiUrl baseApiUrl: String = DEFAULT_BASE_API_URL,
        @DefaultOkHttpClient okHttpClient: OkHttpClient
    ): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(baseApiUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(RETROFIT_CONVERTER_CONTENT_TYPE.toMediaType()))
            .addCallAdapterFactory(NetworkResultCallAdapterFactory())
            .build()
    }
}