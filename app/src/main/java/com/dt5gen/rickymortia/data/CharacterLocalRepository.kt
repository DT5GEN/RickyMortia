package com.dt5gen.rickymortia.data

import com.dt5gen.rickymortia.data.local.CharacterDao
import com.dt5gen.rickymortia.data.local.CharacterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterLocalRepository @Inject constructor(
    private val dao: CharacterDao
) {
    fun countFlow(): Flow<Int> = dao.countFlow()

    suspend fun seedOneIfEmpty() {
        // Добавим одну запись, чтобы увидеть, как меняется count
        // В реальном коде это будет API → DB, а не seed
        val seed = CharacterEntity(
            id = 1L,
            name = "Rick Sanchez",
            species = "Human",
            status = "Alive",
            gender = "Male",
            imageUrl = null
        )
        dao.insertAll(listOf(seed))
    }
}
