package com.evaluation.mangascope.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.evaluation.mangascope.data.local.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {
    @Upsert
    suspend fun insertAll(remoteKeys: List<RemoteKeyEntity>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun remoteKeyId(id: String): RemoteKeyEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun clearAll()
}
