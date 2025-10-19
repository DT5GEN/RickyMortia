package com.dt5gen.rickymortia.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dt5gen.rickymortia.data.local.CharacterDao
import com.dt5gen.rickymortia.data.local.CharacterEntity
import com.dt5gen.rickymortia.data.remote.dto.CharacterDto
import com.dt5gen.rickymortia.data.remote.dto.toEntity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject



class CharacterPagingSource @Inject constructor(
    private val api: RickAndMortyApi,
    private val dao: CharacterDao
) : PagingSource<Int, CharacterEntity>() {

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor)
        return page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
        val page = params.key ?: 1

        return try {
            // page — Int (как и в официальном API)
            val response = api.getCharacters(page = page)

            // 1) id текущей страницы
            val ids: List<Long> = response.results.mapNotNull { dtoIdToLong(it) }

            // 2) старые значения из БД (для переноса isLiked/isStudied/studiedCorrect)
            val oldMap: Map<Long, CharacterEntity> =
                if (ids.isEmpty()) emptyMap()
                else dao.getByIds(ids).associateBy { it.id }

            // 3) маппим DTO -> Entity, передаём old
            val entities: List<CharacterEntity> = response.results.map { dto ->
                val old = dtoIdToLong(dto)?.let(oldMap::get)
                dto.toEntity(old)
            }

            val totalPages = response.info.pages
            val next = if (page < totalPages) page + 1 else null
            val prev = if (page > 1) page - 1 else null

            LoadResult.Page(
                data = entities,
                prevKey = prev,
                nextKey = next
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }


    private fun dtoIdToLong(dto: CharacterDto): Long? =
        dto.id?.toString()?.toLongOrNull()
}