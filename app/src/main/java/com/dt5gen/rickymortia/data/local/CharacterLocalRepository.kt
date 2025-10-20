package com.dt5gen.rickymortia.data.local

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Локальный репозиторий поверх Room-DAO.
 */
@Singleton
class CharacterLocalRepository @Inject constructor(
    private val dao: CharacterDao
) {

    // --- чтение/потоки/пэйджинг ---

    fun countFlow(): Flow<Int> = dao.countFlow()

    fun pagingSource(): PagingSource<Int, CharacterEntity> = dao.pagingSource()

    // --- запись/мутации ---

    suspend fun insertAll(items: List<CharacterEntity>) = dao.insertAll(items)

    suspend fun clearAll() = dao.clearAll()

    /** Лайк/анлайк персонажа. */
    suspend fun setLiked(id: Long, liked: Boolean) = dao.setFavorite(id, liked)

    /** Отметить/снять «изучено». */
    suspend fun setStudied(id: Long, studied: Boolean) = dao.setStudied(id, studied)
}