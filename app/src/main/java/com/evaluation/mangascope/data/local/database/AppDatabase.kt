package com.evaluation.mangascope.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.evaluation.mangascope.data.local.dao.CacheMetadataDao
import com.evaluation.mangascope.data.local.dao.MangaDao
import com.evaluation.mangascope.data.local.dao.RemoteKeyDao
import com.evaluation.mangascope.data.local.dao.UserDao
import com.evaluation.mangascope.data.local.entity.CacheMetadataEntity
import com.evaluation.mangascope.data.local.entity.FavoriteMangaEntity
import com.evaluation.mangascope.data.local.entity.MangaEntity
import com.evaluation.mangascope.data.local.entity.RemoteKeyEntity
import com.evaluation.mangascope.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class, MangaEntity::class, CacheMetadataEntity::class, RemoteKeyEntity::class,
        FavoriteMangaEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cacheMetadataDao(): CacheMetadataDao

    abstract fun mangaDao(): MangaDao

    abstract fun remoteKeyDao(): RemoteKeyDao

    abstract fun userDao(): UserDao
}
