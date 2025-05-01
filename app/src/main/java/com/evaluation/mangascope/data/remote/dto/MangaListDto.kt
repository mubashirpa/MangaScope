package com.evaluation.mangascope.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MangaListDto(
    val code: Int? = null,
    val `data`: List<MangaDto>? = null,
)
