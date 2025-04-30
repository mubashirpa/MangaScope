package com.evaluation.mangascope.domain.usecase

import com.evaluation.mangascope.R
import com.evaluation.mangascope.core.Result
import com.evaluation.mangascope.core.UiText
import com.evaluation.mangascope.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class IsUserSignedInUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke(): Flow<Result<Boolean>> =
        flow {
            try {
                emit(Result.Loading())
                val email = authenticationRepository.signInPreferencesFlow.first().email
                emit(Result.Success(email != null))
            } catch (_: Exception) {
                emit(Result.Error(UiText.StringResource(R.string.error_unexpected)))
            }
        }
}
