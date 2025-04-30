package com.evaluation.mangascope.di

import androidx.room.Room
import com.evaluation.mangascope.data.local.database.AppDatabase
import com.evaluation.mangascope.data.repository.AuthenticationRepositoryImpl
import com.evaluation.mangascope.data.repository.dataStore
import com.evaluation.mangascope.domain.repository.AuthenticationRepository
import com.evaluation.mangascope.domain.usecase.IsUserSignedInUseCase
import com.evaluation.mangascope.domain.usecase.SignInUseCase
import com.evaluation.mangascope.presentation.main.MainViewModel
import com.evaluation.mangascope.presentation.signIn.SignInViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        single {
            val database =
                Room
                    .databaseBuilder(
                        androidContext(),
                        AppDatabase::class.java,
                        "manga_scope_db",
                    ).build()
            database.userDao()
        }
        single { androidContext().dataStore }
        singleOf(::AuthenticationRepositoryImpl) { bind<AuthenticationRepository>() }
        singleOf(::SignInUseCase)
        singleOf(::IsUserSignedInUseCase)
        viewModelOf(::MainViewModel)
        viewModelOf(::SignInViewModel)
    }
