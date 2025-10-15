package com.dt5gen.rickymortia.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val species: String?,
    val status: String?,
    val gender: String?,
    val imageUrl: String?
)
