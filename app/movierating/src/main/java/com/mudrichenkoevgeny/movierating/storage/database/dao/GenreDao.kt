package com.mudrichenkoevgeny.movierating.storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mudrichenkoevgeny.movierating.storage.database.model.GenreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genres: List<GenreEntity>)

    @Query(value = "SELECT * FROM genres")
    fun getAllFlow(): Flow<List<GenreEntity>>

    @Query(value = "SELECT * FROM genres")
    suspend fun getAll(): List<GenreEntity>

    @Query(value = "DELETE FROM genres")
    suspend fun clear()
}