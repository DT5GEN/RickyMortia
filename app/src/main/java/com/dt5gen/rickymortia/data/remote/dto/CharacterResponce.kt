package com.dt5gen.rickymortia.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharactersResponse(
    @Json(name = "info") val info: InfoDto,
    @Json(name = "results") val results: List<CharacterDto>
)

@JsonClass(generateAdapter = true)
data class InfoDto(
    @Json(name = "count") val count: Int,
    @Json(name = "pages") val pages: Int,
    @Json(name = "next") val next: String?,
    @Json(name = "prev") val prev: String?
)