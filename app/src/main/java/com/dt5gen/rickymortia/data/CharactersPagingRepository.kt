package com.dt5gen.rickymortia.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import com.dt5gen.rickymortia.data.local.CharacterDao
import com.dt5gen.rickymortia.data.local.CharacterEntity
import com.dt5gen.rickymortia.data.remote.CharacterPagingSource
import com.dt5gen.rickymortia.data.remote.RickAndMortyApi

@Singleton
class CharactersPagingRepository @Inject constructor(
    private val api: RickAndMortyApi,
    private val dao: CharacterDao
) {


    fun pagedCharacters(
        pageSize: Int = 20,
        query: String? = null
    ): Flow<PagingData<CharacterEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CharacterPagingSource(api, dao) }
        ).flow
    }
}