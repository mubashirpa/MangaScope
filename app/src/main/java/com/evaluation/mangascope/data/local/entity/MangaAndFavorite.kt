package com.evaluation.mangascope.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.evaluation.mangascope.domain.model.manga.Manga

data class MangaAndFavorite(
    @Embedded val manga: Manga,
    @Relation(
        parentColumn = "id",
        entityColumn = "mangaId",
    )
    val favoriteManga: FavoriteMangaEntity?,
)
