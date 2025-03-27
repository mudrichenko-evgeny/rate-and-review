package com.mudrichenkoevgeny.movierating.storage.database.converter

import androidx.room.TypeConverter
import com.mudrichenkoevgeny.core.model.domain.Review
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ReviewConverters {

    @TypeConverter
    fun fromReview(review: Review?): String? {
        return review?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toReview(reviewJson: String?): Review? {
        return reviewJson?.let { Json.decodeFromString<Review>(it) }
    }
}