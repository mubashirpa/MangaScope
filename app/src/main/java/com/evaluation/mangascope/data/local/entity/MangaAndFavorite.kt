package com.evaluation.mangascope.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class MangaAndFavorite(
    @Embedded val manga: MangaEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "mangaId",
    )
    val favoriteManga: FavoriteMangaEntity?,
)
