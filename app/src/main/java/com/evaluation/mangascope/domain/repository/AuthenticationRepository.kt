package com.evaluation.mangascope.domain.repository

import com.evaluation.mangascope.data.local.entity.UserEntity
import com.evaluation.mangascope.domain.model.preferences.SignInPreferences
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val signInPreferencesFlow: Flow<SignInPreferences>

    suspend fun signIn(
        email: String,
        password: String,
    )

    suspend fun getUserByEmail(email: String): UserEntity?
}
