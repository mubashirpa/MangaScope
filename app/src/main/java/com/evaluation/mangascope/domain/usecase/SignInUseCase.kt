package com.evaluation.mangascope.domain.usecase

import com.evaluation.mangascope.R
import com.evaluation.mangascope.core.Result
import com.evaluation.mangascope.core.UiText
import com.evaluation.mangascope.domain.repository.AuthenticationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class SignInUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    operator fun invoke(
        email: String,
        password: String,
    ): Flow<Result<Boolean>> =
        flow {
            try {
                emit(Result.Loading())
                withContext(ioDispatcher) { authenticationRepository.signIn(email, password) }
                emit(Result.Success(true))
            } catch (_: Exception) {
                emit(Result.Error(UiText.StringResource(R.string.error_sign_in)))
            }
        }
}
