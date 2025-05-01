package com.evaluation.mangascope.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MangaDto(
    val authors: List<String>? = null,
    @SerialName("create_at")
    val createAt: Long? = null,
    val genres: List<String>? = null,
    val id: String? = null,
    val nsfw: Boolean? = null,
    val status: String? = null,
    @SerialName("sub_title")
    val subTitle: String? = null,
    val summary: String? = null,
    val thumb: String? = null,
    val title: String? = null,
    @SerialName("total_chapter")
    val totalChapter: Int? = null,
    val type: String? = null,
    @SerialName("update_at")
    val updateAt: Long? = null,
)
