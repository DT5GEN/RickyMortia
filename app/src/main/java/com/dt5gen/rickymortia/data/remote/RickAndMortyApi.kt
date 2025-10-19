package com.dt5gen.rickymortia.data.remote

import com.dt5gen.rickymortia.data.remote.dto.CharactersResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Rick & Morty API
 * https://rickandmortyapi.com/documentation
 */
interface RickAndMortyApi {


    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int = 1
    ): CharactersResponse
}