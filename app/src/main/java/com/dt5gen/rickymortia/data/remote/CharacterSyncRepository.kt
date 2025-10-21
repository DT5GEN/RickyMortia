package com.dt5gen.rickymortia.data.remote

import com.dt5gen.rickymortia.data.local.CharacterDao
import com.dt5gen.rickymortia.data.local.CharacterEntity
import com.dt5gen.rickymortia.data.remote.dto.toEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterSyncRepository @Inject constructor(
    private val api: RickAndMortyApi,
    private val dao: CharacterDao
) {


    suspend fun syncFirstPage() {
        val page = api.getCharacters(page = 1)

        val ids = page.results.mapNotNull { it.id?.toString()?.toLongOrNull() }
        val oldMap: Map<Long, CharacterEntity> =
            if (ids.isEmpty()) emptyMap()
            else dao.getByIds(ids).associateBy { it.id.toLong() }

        val entities = page.results.map { dto ->
            val old = dto.id?.toString()?.toLongOrNull()?.let { oldMap[it] }
            dto.toEntity(old)
        }

        dao.clearAll()
        dao.insertAll(entities)
    }
}