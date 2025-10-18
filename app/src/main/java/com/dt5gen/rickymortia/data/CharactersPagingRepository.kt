package com.dt5gen.rickymortia.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dt5gen.rickymortia.data.local.CharacterDao
import com.dt5gen.rickymortia.data.local.CharacterEntity
import com.dt5gen.rickymortia.data.local.CharacterLocalRepository
import com.dt5gen.rickymortia.data.remote.RickAndMortyApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий пейджинга.
 */

@Singleton
class CharactersPagingRepository @Inject
    constructor(
    private val api: RickAndMortyApi,
    private val localRepo: CharacterLocalRepository,
    private val dao: CharacterDao
) {

    fun pagedCharacters(
        pageSize: Int = 20,
        query: String? = null // CHANGE
    ): Flow<PagingData<CharacterEntity>> {
        val source = if (query.isNullOrBlank()) {
            { dao.pagingSource() }
        } else {
            { dao.pagingSourceFiltered(query.trim()) }
        }
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
                prefetchDistance = pageSize / 2
            ),
            pagingSourceFactory = source
        ).flow
    }
}