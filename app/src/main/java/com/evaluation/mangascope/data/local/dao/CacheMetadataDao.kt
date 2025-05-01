package com.evaluation.mangascope.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.evaluation.mangascope.data.local.entity.CacheMetadataEntity

@Dao
interface CacheMetadataDao {
    @Upsert
    suspend fun insert(metadata: CacheMetadataEntity)

    @Query("SELECT lastUpdated FROM cache_metadata WHERE id = :id")
    suspend fun getLastUpdatedById(id: String): Long?

    @Query("DELETE FROM cache_metadata WHERE id = :id")
    suspend fun deleteMetadataById(id: String)
}
