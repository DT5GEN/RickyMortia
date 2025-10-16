package com.dt5gen.rickymortia.data.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterLocalRepository @Inject constructor(
    private val dao: CharacterDao
) {
    fun countFlow(): Flow<Int> = dao.countFlow()

    suspend fun seedOneIfEmpty() {

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