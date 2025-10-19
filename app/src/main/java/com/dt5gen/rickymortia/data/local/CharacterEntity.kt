package com.dt5gen.rickymortia.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Long,

    // Поля с сервера
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val imageUrl: String,


    val originName: String,
    val originUrl: String,
    val locationName: String,
    val locationUrl: String,


    val episodesCsv: String,
    val apiUrl: String,
    val createdIso: String,

    // Локальные поля (сохраняем при синхронизации)
    val isFavorite: Boolean = false,
    val isStudied: Boolean = false,
    val studiedCorrect: Int = 0
)