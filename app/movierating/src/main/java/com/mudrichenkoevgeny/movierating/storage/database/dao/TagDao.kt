package com.mudrichenkoevgeny.movierating.storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mudrichenkoevgeny.movierating.storage.database.model.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tags: List<TagEntity>)

    @Query(value = "SELECT * FROM tags")
    fun getAllFlow(): Flow<List<TagEntity>>

    @Query(value = "SELECT * FROM tags")
    suspend fun getAll(): List<TagEntity>

    @Query(value = "DELETE FROM tags")
    suspend fun clear()
}