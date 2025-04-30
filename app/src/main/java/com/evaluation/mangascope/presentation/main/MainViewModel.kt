package com.evaluation.mangascope.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evaluation.mangascope.core.Result
import com.evaluation.mangascope.domain.usecase.IsUserSignedInUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(
    private val isUserSignedInUseCase: IsUserSignedInUseCase,
) : ViewModel() {
    var uiState by mutableStateOf(MainUiState())
        private set

    init {
        checkIsUserSignedIn()
    }

    private fun checkIsUserSignedIn() {
        isUserSignedInUseCase()
            .onEach { result ->
                when (result) {
                    is Result.Empty -> {}

                    is Result.Error -> {
                        uiState =
                            uiState.copy(
                                isLoading = false,
                                isUserSignedIn = false,
                            )
                    }

                    is Result.Loading -> {
                        uiState = uiState.copy(isLoading = true)
                    }

                    is Result.Success -> {
                        val isUserSignedIn = result.data!!
                        uiState =
                            uiState.copy(
                                isLoading = false,
                                isUserSignedIn = isUserSignedIn,
                            )
                    }
                }
            }.launchIn(viewModelScope)
    }
}
