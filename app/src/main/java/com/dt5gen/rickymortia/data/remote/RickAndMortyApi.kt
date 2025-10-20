package com.dt5gen.rickymortia.data.remote

import com.dt5gen.rickymortia.data.remote.dto.CharacterDto
import com.dt5gen.rickymortia.data.remote.dto.CharactersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): CharactersResponse

    /** Детали одного персонажа (для экрана Details). */
    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Long): CharacterDto
}