package com.evaluation.mangascope.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.evaluation.mangascope.data.local.database.AppDatabase
import com.evaluation.mangascope.data.local.entity.CacheMetadataEntity
import com.evaluation.mangascope.data.local.entity.MangaEntity
import com.evaluation.mangascope.data.local.entity.RemoteKeyEntity
import com.evaluation.mangascope.data.mapper.toMangaEntityList
import com.evaluation.mangascope.domain.repository.MangaType
import com.evaluation.mangascope.domain.repository.MangaVerseRepository
import java.io.IOException
import java.util.concurrent.TimeUnit

private const val MANGA_TABLE_ID = "manga"

@OptIn(ExperimentalPagingApi::class)
class MangaRemoteMediator(
    private val database: AppDatabase,
    private val repository: MangaVerseRepository,
    private val page: Int,
    private val genres: List<String>,
    private val nsfw: Boolean,
    private val type: MangaType,
) : RemoteMediator<Int, MangaEntity>() {
    private val cacheMetadataDao = database.cacheMetadataDao()
    private val mangaDao = database.mangaDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        val lastUpdated = cacheMetadataDao.getLastUpdatedById(MANGA_TABLE_ID) ?: 0
        return if (System.currentTimeMillis() - lastUpdated <= cacheTimeout) {
            // Cached data is fresh (within 1 hour), skip refresh
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Cached data is stale or missing, trigger refresh
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MangaEntity>,
    ): MediatorResult {
        return try {
            val loadKey =
                when (loadType) {
                    LoadType.REFRESH -> {
                        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                        remoteKeys?.nextKey?.minus(1) ?: page
                    }

                    LoadType.PREPEND -> {
                        val remoteKeys = getRemoteKeyForFirstItem(state)
                        val prevKey = remoteKeys?.prevKey
                        if (prevKey == null) {
                            return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                        }
                        prevKey
                    }

                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)
                        val nextKey = remoteKeys?.nextKey
                        if (nextKey == null) {
                            return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                        }
                        nextKey
                    }
                }

            val response =
                repository.getManga(
                    page = loadKey,
                    genres = genres,
                    nsfw = nsfw,
                    type = type,
                )
            val manga = response.toMangaEntityList()
            val endOfPaginationReached = manga.isEmpty()

            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearAll()
                    cacheMetadataDao.deleteMetadataById(MANGA_TABLE_ID)
                    mangaDao.clearAll()
                }

                val prevKey = if (loadKey == page) null else loadKey - 1
                val nextKey = if (endOfPaginationReached) null else loadKey + 1

                val keys =
                    manga.map {
                        RemoteKeyEntity(
                            id = it.id,
                            prevKey = prevKey,
                            nextKey = nextKey,
                        )
                    }
                remoteKeyDao.insertAll(keys)
                mangaDao.insertAll(manga)
                cacheMetadataDao.insert(
                    CacheMetadataEntity(
                        id = MANGA_TABLE_ID,
                        lastUpdated = System.currentTimeMillis(),
                    ),
                )
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MangaEntity>): RemoteKeyEntity? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data
            ?.lastOrNull()
            ?.let { manga ->
                // Get the remote keys of the last item retrieved
                remoteKeyDao.remoteKeyId(manga.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MangaEntity>): RemoteKeyEntity? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data
            ?.firstOrNull()
            ?.let { manga ->
                // Get the remote keys of the first items retrieved
                remoteKeyDao.remoteKeyId(manga.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MangaEntity>): RemoteKeyEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { mangaId ->
                remoteKeyDao.remoteKeyId(mangaId)
            }
        }
    }
}
