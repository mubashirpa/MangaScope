package com.evaluation.mangascope.domain.usecase

import com.evaluation.mangascope.data.mapper.toManga
import com.evaluation.mangascope.domain.repository.MangaVerseRepository
import kotlinx.coroutines.flow.map

class GetMangaDetailsUseCase(
    private val repository: MangaVerseRepository,
) {
    suspend operator fun invoke(id: String) = repository.getMangaDetails(id).map { it.toManga() }
}
