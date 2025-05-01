package com.evaluation.mangascope.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.evaluation.mangascope.data.local.entity.MangaEntity

@Dao
interface MangaDao {
    @Upsert
    suspend fun insertAll(manga: List<MangaEntity>)

    @Query("SELECT * FROM manga")
    fun pagingSource(): PagingSource<Int, MangaEntity>

    @Query("DELETE FROM manga")
    suspend fun clearAll()
}
