package com.dt5gen.rickymortia.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InfoDto(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

@JsonClass(generateAdapter = true)
data class CharacterDto(
    val id: Long,
    val name: String,
    val status: String?,
    val species: String?,
    val type: String?,
    val gender: String?,
    val image: String?
)

@JsonClass(generateAdapter = true)
data class CharactersPageDto(
    val info: InfoDto,
    val results: List<CharacterDto>
)
