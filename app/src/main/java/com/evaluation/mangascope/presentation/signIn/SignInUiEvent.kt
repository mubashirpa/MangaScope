package com.evaluation.mangascope.presentation.signIn

sealed class SignInUiEvent {
    data object SignIn : SignInUiEvent()

    data object UserMessageShown : SignInUiEvent()
}
