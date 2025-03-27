package com.mudrichenkoevgeny.movierating.storage.database.model

import android.annotation.SuppressLint
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey val id: String,
    val name: String
)