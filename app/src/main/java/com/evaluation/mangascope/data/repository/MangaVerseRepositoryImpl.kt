package com.evaluation.mangascope.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.evaluation.mangascope.BuildConfig
import com.evaluation.mangascope.core.Constants
import com.evaluation.mangascope.data.local.database.AppDatabase
import com.evaluation.mangascope.data.local.entity.MangaEntity
import com.evaluation.mangascope.data.remote.dto.MangaListDto
import com.evaluation.mangascope.data.remote.paging.MangaRemoteMediator
import com.evaluation.mangascope.domain.repository.MangaType
import com.evaluation.mangascope.domain.repository.MangaVerseRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.flow.Flow

class MangaVerseRepositoryImpl(
    private val httpClient: HttpClient,
    private val database: AppDatabase,
) : MangaVerseRepository {
    override suspend fun getManga(
        page: Int,
        genres: List<String>,
        nsfw: Boolean,
        type: MangaType,
    ): MangaListDto =
        httpClient
            .get(Constants.MANGAVERSE_API_BASE_URL) {
                url {
                    appendPathSegments("manga", "fetch")
                    parameters.append("page", page.toString())
                    if (genres.isNotEmpty()) {
                        parameters.append("genres", genres.joinToString(","))
                    }
                    parameters.append("nsfw", nsfw.toString())
                    parameters.append("type", type.name.uppercase())
                }
                header("x-rapidapi-key", BuildConfig.RAPID_API_KEY)
                header("x-rapidapi-host", "mangaverse-api.p.rapidapi.com")
            }.body()

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getMangaPaging(
        page: Int,
        genres: List<String>,
        nsfw: Boolean,
        type: MangaType,
    ): Flow<PagingData<MangaEntity>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator =
                MangaRemoteMediator(
                    database = database,
                    repository = this,
                    page = page,
                    genres = genres,
                    nsfw = nsfw,
                    type = type,
                ),
            pagingSourceFactory = {
                database.mangaDao().pagingSource()
            },
        ).flow
}
