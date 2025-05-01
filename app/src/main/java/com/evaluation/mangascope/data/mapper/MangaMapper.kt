package com.evaluation.mangascope.data.mapper

import com.evaluation.mangascope.data.local.entity.MangaEntity
import com.evaluation.mangascope.data.remote.dto.MangaListDto
import com.evaluation.mangascope.domain.model.manga.Manga

fun MangaListDto.toMangaEntityList(): List<MangaEntity> =
    data
        ?.map {
            MangaEntity(
                id = it.id!!,
                subTitle = it.subTitle,
                summary = it.summary,
                thumb = it.thumb,
                title = it.title,
            )
        }.orEmpty()

fun MangaEntity.toManga(): Manga =
    Manga(
        id = id,
        subTitle = subTitle,
        summary = summary,
        thumb = thumb,
        title = title,
    )
