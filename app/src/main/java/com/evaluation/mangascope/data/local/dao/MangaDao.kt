package com.evaluation.mangascope.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.evaluation.mangascope.data.local.entity.FavoriteMangaEntity
import com.evaluation.mangascope.data.local.entity.MangaAndFavorite
import com.evaluation.mangascope.data.local.entity.MangaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaDao {
    @Upsert
    suspend fun insertAll(manga: List<MangaEntity>)

    @Query("SELECT * FROM manga")
    fun pagingSource(): PagingSource<Int, MangaEntity>

    @Query("DELETE FROM manga")
    suspend fun clearAll()

    @Transaction
    @Query("SELECT * FROM manga WHERE id = :id")
    fun getMangaDetailsById(id: String): Flow<MangaAndFavorite>

    @Upsert
    suspend fun updateFavorite(favoriteManga: FavoriteMangaEntity)
}
