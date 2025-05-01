package com.evaluation.mangascope.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.evaluation.mangascope.data.mapper.toManga
import com.evaluation.mangascope.domain.model.manga.Manga
import com.evaluation.mangascope.domain.repository.MangaVerseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMangaUseCase(
    private val repository: MangaVerseRepository,
) {
    suspend operator fun invoke(): Flow<PagingData<Manga>> =
        repository.getMangaPaging().map {
            it.map { it.toManga() }
        }
}
