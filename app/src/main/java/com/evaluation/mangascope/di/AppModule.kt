package com.evaluation.mangascope.di

import androidx.room.Room
import com.evaluation.mangascope.data.local.database.AppDatabase
import com.evaluation.mangascope.data.repository.AuthenticationRepositoryImpl
import com.evaluation.mangascope.data.repository.MangaVerseRepositoryImpl
import com.evaluation.mangascope.data.repository.dataStore
import com.evaluation.mangascope.domain.repository.AuthenticationRepository
import com.evaluation.mangascope.domain.repository.MangaVerseRepository
import com.evaluation.mangascope.domain.usecase.GetMangaDetailsUseCase
import com.evaluation.mangascope.domain.usecase.GetMangaUseCase
import com.evaluation.mangascope.domain.usecase.IsUserSignedInUseCase
import com.evaluation.mangascope.domain.usecase.SignInUseCase
import com.evaluation.mangascope.domain.usecase.UpdateMangaFavoriteUseCase
import com.evaluation.mangascope.presentation.main.MainViewModel
import com.evaluation.mangascope.presentation.manga.MangaViewModel
import com.evaluation.mangascope.presentation.signIn.SignInViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        single {
            HttpClient {
                expectSuccess = true
                install(ContentNegotiation) {
                    json(
                        Json {
                            isLenient = true
                            ignoreUnknownKeys = true
                            useAlternativeNames = false
                        },
                    )
                }
            }
        }
        single {
            Room
                .databaseBuilder(
                    androidContext(),
                    AppDatabase::class.java,
                    "manga_scope_db",
                ).build()
        }
        single {
            val database: AppDatabase = get()
            database.userDao()
        }
        single { androidContext().dataStore }
        singleOf(::AuthenticationRepositoryImpl) { bind<AuthenticationRepository>() }
        singleOf(::MangaVerseRepositoryImpl) { bind<MangaVerseRepository>() }
        singleOf(::SignInUseCase)
        singleOf(::IsUserSignedInUseCase)
        singleOf(::GetMangaUseCase)
        singleOf(::GetMangaDetailsUseCase)
        singleOf(::UpdateMangaFavoriteUseCase)
        viewModelOf(::MainViewModel)
        viewModelOf(::SignInViewModel)
        viewModelOf(::MangaViewModel)
    }
