package com.mudrichenkoevgeny.movierating.storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mudrichenkoevgeny.movierating.storage.database.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Query(value = "SELECT * FROM movies")
    fun getAllFlow(): Flow<List<MovieEntity>>

    @Query(value = "SELECT * FROM movies")
    suspend fun getAll(): List<MovieEntity>

    @Query(value = "SELECT * FROM movies WHERE id = :id")
    suspend fun getById(id: String): MovieEntity?

    @Query(value = "DELETE FROM movies")
    suspend fun clear()
}