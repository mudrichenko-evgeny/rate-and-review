package com.mudrichenkoevgeny.movierating.di

import com.mudrichenkoevgeny.core.common.di.BaseApiUrl
import com.mudrichenkoevgeny.core.common.di.IsMockRestApi
import com.mudrichenkoevgeny.movierating.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieRatingConfigModule {

    @BaseApiUrl
    @Provides
    @Singleton
    fun provideBaseApiUrl(): String = BuildConfig.BASE_API_URL

    @IsMockRestApi
    @Provides
    @Singleton
    fun provideIsMockRestApi(): Boolean = BuildConfig.IS_MOCK_REST_API
}