package com.mudrichenkoevgeny.movierating.storage.database.converter

import androidx.room.TypeConverter
import com.mudrichenkoevgeny.movierating.storage.database.model.GenreEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GenreConverters {

    @TypeConverter
    fun fromGenreList(genres: List<GenreEntity>): String {
        return Json.encodeToString(genres)
    }

    @TypeConverter
    fun toGenreList(genresJson: String): List<GenreEntity> {
        return Json.decodeFromString(genresJson)
    }
}