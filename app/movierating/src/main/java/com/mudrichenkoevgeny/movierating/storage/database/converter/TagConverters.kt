package com.mudrichenkoevgeny.movierating.storage.database.converter

import androidx.room.TypeConverter
import com.mudrichenkoevgeny.movierating.storage.database.model.TagEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TagConverters {

    @TypeConverter
    fun fromGenreList(genres: List<TagEntity>): String {
        return Json.encodeToString(genres)
    }

    @TypeConverter
    fun toGenreList(genresJson: String): List<TagEntity> {
        return Json.decodeFromString(genresJson)
    }
}