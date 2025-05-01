package com.evaluation.mangascope.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_manga")
data class FavoriteMangaEntity(
    @PrimaryKey val mangaId: String,
    val isFavorite: Boolean?,
)
