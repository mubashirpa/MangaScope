package com.evaluation.mangascope.presentation.mangaDetails

sealed class MangaDetailsUiEvent {
    data class OnMangaFavoriteChange(
        val isFavorite: Boolean,
    ) : MangaDetailsUiEvent()
}
