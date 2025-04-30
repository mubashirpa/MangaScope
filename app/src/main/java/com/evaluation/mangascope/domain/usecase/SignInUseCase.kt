package com.evaluation.mangascope.domain.usecase

import com.evaluation.mangascope.R
import com.evaluation.mangascope.core.Result
import com.evaluation.mangascope.core.UiText
import com.evaluation.mangascope.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke(
        email: String,
        password: String,
    ): Flow<Result<Boolean>> =
        flow {
            try {
                emit(Result.Loading())
                authenticationRepository.signIn(email, password)
                emit(Result.Success(true))
            } catch (_: Exception) {
                emit(Result.Error(UiText.StringResource(R.string.error_sign_in)))
            }
        }
}
