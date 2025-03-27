package com.mudrichenkoevgeny.core.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenRefreshOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultOkHttpInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingOkHttpInterceptor