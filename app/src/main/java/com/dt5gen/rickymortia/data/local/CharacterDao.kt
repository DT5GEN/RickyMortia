package com.dt5gen.rickymortia.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CharacterEntity>)

    @Query("DELETE FROM characters")
    suspend fun clearAll()
    @Query("SELECT COUNT(*) FROM characters")
    fun countFlow(): Flow<Int>



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

    @Upsert
    suspend fun upsertAll(items: List<CharacterEntity>)

    // --- Избранное / «выучено» / счётчик викторины ---
    @Query("UPDATE characters SET isFavorite = :value WHERE id = :id")
    suspend fun setFavorite(id: Long, value: Boolean)

    @Query("UPDATE characters SET isStudied = :value WHERE id = :id")
    suspend fun setStudied(id: Long, value: Boolean)

    @Query("UPDATE characters SET studiedCorrect = :count WHERE id = :id")
    suspend fun setStudiedCorrect(id: Int, count: Int)

    // -------- ДОБАВЛЕНО: чтение существующих сущностей пачкой по id --------
    @Query("SELECT * FROM characters WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<Long>): List<CharacterEntity>
}