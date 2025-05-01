package com.evaluation.mangascope.presentation.mangaDetails

import com.evaluation.mangascope.domain.model.manga.Manga

data class MangaDetailsUiState(
    val manga: Manga? = null,
    val isFavorite: Boolean = false,
)
