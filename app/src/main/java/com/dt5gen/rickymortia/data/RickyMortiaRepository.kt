package com.dt5gen.rickymortia.data

import com.dt5gen.rickymortia.data.local.CharacterDao
import javax.inject.Inject
import javax.inject.Singleton

/** Контракт репозитория приложения. */
interface RickyMortiaRepository {
    fun message(): String

    /** Лайк / анлайк персонажа. */
    suspend fun toggleLiked(id: Long, liked: Boolean)

    /** Пометить как изученного / снять пометку. */
    suspend fun toggleStudied(id: Long, studied: Boolean)
}

/** Реализация, работающая поверх Room DAO. */
@Singleton
class RickyMortiaRepositoryImpl @Inject constructor(
    private val dao: CharacterDao
) : RickyMortiaRepository {

    override fun message(): String = "Hello from Hilt DI"

    override suspend fun toggleLiked(id: Long, liked: Boolean) {
        dao.setLiked(id, liked)
    }

    override suspend fun toggleStudied(id: Long, studied: Boolean) {
        dao.setStudied(id, studied)
    }
}