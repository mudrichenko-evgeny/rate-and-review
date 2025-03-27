package com.mudrichenkoevgeny.movierating.di

import com.mudrichenkoevgeny.core.common.di.IsMockRestApi
import com.mudrichenkoevgeny.feature.user.repository.UserRepository
import com.mudrichenkoevgeny.movierating.network.restapi.MockMovieRestApi
import com.mudrichenkoevgeny.movierating.network.restapi.MovieRestApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieRatingApiModule {

    @Provides
    @Singleton
    fun provideMovieRestApi(
        @IsMockRestApi isMockRestApi: Boolean,
        userRepository: UserRepository,
        retrofit: Retrofit
    ): MovieRestApi {
        return if (isMockRestApi) {
            MockMovieRestApi(userRepository)
        } else {
            retrofit.create(MovieRestApi::class.java)
        }
    }
}