package com.evaluation.mangascope.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manga")
data class MangaEntity(
    @PrimaryKey val id: String,
    val subTitle: String?,
    val summary: String?,
    val thumb: String?,
    val title: String?,
)
