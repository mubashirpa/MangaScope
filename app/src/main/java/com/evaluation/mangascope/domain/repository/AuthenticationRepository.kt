package com.evaluation.mangascope.domain.repository

interface AuthenticationRepository {
    suspend fun signIn(
        email: String,
        password: String,
    )

    suspend fun isUserSignedIn(email: String): Boolean
}
