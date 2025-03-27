package com.mudrichenkoevgeny.movierating.di

import android.content.Context
import androidx.room.Room
import com.mudrichenkoevgeny.movierating.storage.database.MovieDatabase
import com.mudrichenkoevgeny.movierating.storage.database.dao.GenreDao
import com.mudrichenkoevgeny.movierating.storage.database.dao.MovieDao
import com.mudrichenkoevgeny.movierating.storage.database.dao.TagDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieRatingDatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(
        @ApplicationContext context: Context
    ): MovieDatabase = createMovieDatabase(context)

    private fun createMovieDatabase(context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            MovieDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideTagDao(database: MovieDatabase): TagDao = database.tagDao()

    @Provides
    fun provideGenreDao(database: MovieDatabase): GenreDao = database.genreDao()

    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao = database.movieDao()
}