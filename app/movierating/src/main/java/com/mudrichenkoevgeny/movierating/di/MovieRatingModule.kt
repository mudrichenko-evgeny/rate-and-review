package com.mudrichenkoevgeny.movierating.di

import com.mudrichenkoevgeny.movierating.network.restapi.MovieRestApi
import com.mudrichenkoevgeny.movierating.repository.movie.MovieRepository
import com.mudrichenkoevgeny.movierating.repository.movie.MovieRepositoryImpl
import com.mudrichenkoevgeny.movierating.storage.database.dao.GenreDao
import com.mudrichenkoevgeny.movierating.storage.database.dao.MovieDao
import com.mudrichenkoevgeny.movierating.storage.database.dao.TagDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieRatingModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        tagDao: TagDao,
        genreDao: GenreDao,
        movieDao: MovieDao,
        movieRestApi: MovieRestApi
    ): MovieRepository {
        return MovieRepositoryImpl(
            tagDao,
            genreDao,
            movieDao,
            movieRestApi
        )
    }
}