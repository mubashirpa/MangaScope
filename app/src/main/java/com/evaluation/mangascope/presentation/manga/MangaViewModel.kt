package com.evaluation.mangascope.presentation.manga

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.evaluation.mangascope.domain.usecase.GetMangaUseCase
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MangaViewModel(
    private val getMangaUseCase: GetMangaUseCase,
) : ViewModel() {
    var uiState by mutableStateOf(MangaUiState())
        private set

    init {
        getManga()
    }

    private fun getManga() {
        viewModelScope.launch {
            getMangaUseCase()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    uiState = uiState.copy(isRefreshing = false)
                    uiState.manga.value = it
                }
        }
    }
}
