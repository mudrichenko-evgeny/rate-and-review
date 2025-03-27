package com.mudrichenkoevgeny.core.common.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseApiUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IsMockRestApi