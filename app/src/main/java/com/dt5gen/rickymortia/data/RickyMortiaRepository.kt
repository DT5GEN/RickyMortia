package com.dt5gen.rickymortia.data

import com.dt5gen.rickymortia.data.local.CharacterDao
import javax.inject.Inject
import javax.inject.Singleton

/** Публичный контракт репозитория для UI. */
interface RickyMortiaRepository {
    fun message(): String

    /** Поменять/снять отметку "избранное". */
    suspend fun toggleLiked(id: Long, liked: Boolean)

    /** Поменять/снять отметку "выучено". */
    suspend fun toggleStudied(id: Long, studied: Boolean)
}

/** Реализация на Room DAO. */
@Singleton
class RickyMortiaRepositoryImpl @Inject constructor(
    private val dao: CharacterDao
) : RickyMortiaRepository {

    override fun message(): String = "Hello from Hilt DI"

    override suspend fun toggleLiked(id: Long, liked: Boolean) {

        dao.setFavorite(id = id, value = liked)
    }

    override suspend fun toggleStudied(id: Long, studied: Boolean) {

        dao.setStudied(id = id, value = studied)
    }
}