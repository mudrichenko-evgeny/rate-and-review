package com.mudrichenkoevgeny.movierating.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mudrichenkoevgeny.movierating.storage.database.converter.GenreConverters
import com.mudrichenkoevgeny.movierating.storage.database.converter.ReviewConverters
import com.mudrichenkoevgeny.movierating.storage.database.converter.TagConverters
import com.mudrichenkoevgeny.movierating.storage.database.dao.GenreDao
import com.mudrichenkoevgeny.movierating.storage.database.dao.MovieDao
import com.mudrichenkoevgeny.movierating.storage.database.dao.TagDao
import com.mudrichenkoevgeny.movierating.storage.database.model.GenreEntity
import com.mudrichenkoevgeny.movierating.storage.database.model.MovieEntity
import com.mudrichenkoevgeny.movierating.storage.database.model.TagEntity

@TypeConverters(
    ReviewConverters::class,
    TagConverters::class,
    GenreConverters::class
)
@Database(
    entities = [
        TagEntity::class,
        GenreEntity::class,
        MovieEntity::class
    ],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun tagDao(): TagDao
    abstract fun genreDao(): GenreDao
    abstract fun movieDao(): MovieDao

    companion object {
        const val DATABASE_NAME = "movie-database"
    }
}