package com.dt5gen.rickymortia.data.remote

import com.dt5gen.rickymortia.data.remote.dto.CharactersPageDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int = 1): CharactersPageDto
}
