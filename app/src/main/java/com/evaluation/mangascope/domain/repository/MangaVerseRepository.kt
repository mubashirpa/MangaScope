package com.evaluation.mangascope.domain.repository

import androidx.paging.PagingData
import com.evaluation.mangascope.data.local.entity.MangaEntity
import com.evaluation.mangascope.data.remote.dto.MangaListDto
import kotlinx.coroutines.flow.Flow

interface MangaVerseRepository {
    suspend fun getManga(
        page: Int = 1,
        genres: List<String> = emptyList(),
        nsfw: Boolean = true,
        type: MangaType = MangaType.ALL,
    ): MangaListDto

    suspend fun getMangaPaging(
        page: Int = 1,
        genres: List<String> = emptyList(),
        nsfw: Boolean = true,
        type: MangaType = MangaType.ALL,
    ): Flow<PagingData<MangaEntity>>
}

enum class MangaType {
    ALL,
    JAPAN,
    CHINA,
    KOREA,
}
