package com.dt5gen.rickymortia.data.remote

import com.dt5gen.rickymortia.data.local.CharacterDao
import com.dt5gen.rickymortia.data.local.CharacterEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterSyncRepository @Inject constructor(
    private val api: RickAndMortyApi,
    private val dao: CharacterDao
) {
    suspend fun syncFirstPage() {
        val page = api.getCharacters(page = 1)
        val entities = page.results.map { dto ->
            CharacterEntity(
                id = dto.id,
                name = dto.name,
                species = dto.species,
                status = dto.status,
                gender = dto.gender,
                imageUrl = dto.image
            )
        }

        dao.clearAll()
        dao.insertAll(entities)
    }
}