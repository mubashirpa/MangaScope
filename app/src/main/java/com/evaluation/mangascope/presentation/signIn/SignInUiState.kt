package com.evaluation.mangascope.presentation.signIn

import androidx.compose.foundation.text.input.TextFieldState
import com.evaluation.mangascope.core.UiText

data class SignInUiState(
    val emailState: TextFieldState = TextFieldState(),
    val isSignInComplete: Boolean = false,
    val openProgressDialog: Boolean = false,
    val passwordState: TextFieldState = TextFieldState(),
    val signInButtonEnabled: Boolean = false,
    val userMessage: UiText? = null,
)
