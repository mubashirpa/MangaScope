package com.evaluation.mangascope.domain.usecase

import com.evaluation.mangascope.domain.repository.MangaVerseRepository

class UpdateMangaFavoriteUseCase(
    private val repository: MangaVerseRepository,
) {
    suspend operator fun invoke(
        id: String,
        isFavorite: Boolean,
    ) = repository.updateFavorite(id, isFavorite)
}
