package com.evaluation.mangascope.presentation.signIn

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evaluation.mangascope.core.Result
import com.evaluation.mangascope.domain.usecase.SignInUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
) : ViewModel() {
    var uiState by mutableStateOf(SignInUiState())
        private set

    private var emailValid: Boolean = false
    private var passwordValid: Boolean = false

    init {
        validateEmail()
        validatePassword()
    }

    fun onEvent(event: SignInUiEvent) {
        when (event) {
            SignInUiEvent.SignIn -> {
                signIn(
                    uiState.emailState.text
                        .trim()
                        .toString(),
                    uiState.passwordState.text
                        .trim()
                        .toString(),
                )
            }

            SignInUiEvent.UserMessageShown -> {
                uiState = uiState.copy(userMessage = null)
            }
        }
    }

    private fun validateEmail() {
        viewModelScope.launch {
            snapshotFlow { uiState.emailState.text }
                .collectLatest { email ->
                    emailValid =
                        email.trim().isNotEmpty() &&
                        Patterns.EMAIL_ADDRESS
                            .matcher(email)
                            .matches()
                    uiState = uiState.copy(signInButtonEnabled = emailValid && passwordValid)
                }
        }
    }

    private fun validatePassword() {
        viewModelScope.launch {
            snapshotFlow { uiState.passwordState.text }
                .collectLatest { password ->
                    passwordValid = password.trim().isNotEmpty()
                    uiState = uiState.copy(signInButtonEnabled = passwordValid && emailValid)
                }
        }
    }

    private fun signIn(
        email: String,
        password: String,
    ) {
        signInUseCase(email, password)
            .onEach { result ->
                when (result) {
                    is Result.Empty -> {}

                    is Result.Error -> {
                        uiState =
                            uiState.copy(
                                openProgressDialog = false,
                                userMessage = result.message,
                            )
                    }

                    is Result.Loading -> {
                        uiState = uiState.copy(openProgressDialog = true)
                    }

                    is Result.Success -> {
                        uiState =
                            uiState.copy(
                                isSignInComplete = true,
                                openProgressDialog = false,
                            )
                    }
                }
            }.launchIn(viewModelScope)
    }
}
