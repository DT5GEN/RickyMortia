package com.dt5gen.rickymortia.data.remote.dto

import com.dt5gen.rickymortia.data.local.CharacterEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterDto(
    val id: Long?,
    val name: String?,
    val status: String?,
    val species: String?,
    val type: String?,
    val gender: String?,
    val origin: SimpleRef?,
    val location: SimpleRef?,
    val image: String?,
    val episode: List<String>?,
    val url: String?,
    val created: String?
) {
    @JsonClass(generateAdapter = true)
    data class SimpleRef(
        val name: String?,
        val url: String?
    )
}

/**  маппинг DTO -> Entity c переносом локальных флагов из старой записи. */
fun CharacterDto.toEntity(old: CharacterEntity?): CharacterEntity = CharacterEntity(
    id = id ?: 0,
    name = name.orEmpty(),
    status = status.orEmpty(),
    species = species.orEmpty(),
    type = type.orEmpty(),
    gender = gender.orEmpty(),
    imageUrl = image.orEmpty(),

    originName = origin?.name.orEmpty(),
    originUrl = origin?.url.orEmpty(),
    locationName = location?.name.orEmpty(),
    locationUrl = location?.url.orEmpty(),

    episodesCsv = (episode ?: emptyList()).joinToString(","),
    apiUrl = url.orEmpty(),
    createdIso = created.orEmpty(),

    // переносим локальные значения (избранное/выучено/счётчик)
    isFavorite = old?.isFavorite ?: false,
    isStudied = old?.isStudied ?: false,
    studiedCorrect = old?.studiedCorrect ?: 0
)