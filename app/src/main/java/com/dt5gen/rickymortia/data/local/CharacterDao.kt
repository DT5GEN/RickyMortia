package com.dt5gen.rickymortia.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CharacterEntity>)

    @Query("SELECT COUNT(*) FROM characters")
    fun countFlow(): Flow<Int>

    @Query("DELETE FROM characters")
    suspend fun clearAll()

    // Базовый источник для пейджинга (весь список)
    @Query("SELECT * FROM characters ORDER BY id ASC")
    fun pagingSource(): PagingSource<Int, CharacterEntity>

    // CHANGE: источник для пейджинга с фильтром по запросу (офлайн-поиск)
    // Поиск по нескольким полям: name/species/status/gender
    @Query("""
        SELECT * 
        FROM characters 
        WHERE 
            name    LIKE '%' || :q || '%' OR 
            species LIKE '%' || :q || '%' OR 
            status  LIKE '%' || :q || '%' OR
            gender  LIKE '%' || :q || '%'
        ORDER BY id ASC
    """)
    fun pagingSourceFiltered(q: String): PagingSource<Int, CharacterEntity>

    // Лайк / изучено
    @Query("UPDATE characters SET is_liked = :liked WHERE id = :id")
    suspend fun setLiked(id: Long, liked: Boolean)

    @Query("UPDATE characters SET is_studied = :studied WHERE id = :id")
    suspend fun setStudied(id: Long, studied: Boolean)
}