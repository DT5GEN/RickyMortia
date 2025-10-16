package com.dt5gen.rickymortia.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dt5gen.rickymortia.data.local.CharacterDao
import com.dt5gen.rickymortia.data.local.CharacterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharactersPagingRepository @Inject constructor(
    private val dao: CharacterDao
) {
    fun pagedCharacters(pageSize: Int = 20): Flow<PagingData<CharacterEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                prefetchDistance = pageSize / 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { dao.pagingSource() }
        ).flow
}
