package com.evaluation.mangascope.presentation.mangaDetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.evaluation.mangascope.domain.usecase.GetMangaDetailsUseCase
import com.evaluation.mangascope.domain.usecase.UpdateMangaFavoriteUseCase
import com.evaluation.mangascope.navigation.Route
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MangaDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getMangaDetailsUseCase: GetMangaDetailsUseCase,
    private val updateMangaFavoriteUseCase: UpdateMangaFavoriteUseCase,
) : ViewModel() {
    var uiState by mutableStateOf(MangaDetailsUiState())
        private set

    private val mangaId = savedStateHandle.toRoute<Route.MangaDetails>().id

    init {
        getMangaDetails()
    }

    fun onEvent(event: MangaDetailsUiEvent) {
        when (event) {
            is MangaDetailsUiEvent.OnMangaFavoriteChange -> {
                updateMangaFavorite(event.isFavorite)
            }
        }
    }

    private fun getMangaDetails() {
        viewModelScope.launch {
            getMangaDetailsUseCase(mangaId).collectLatest { manga ->
                uiState =
                    uiState.copy(
                        isFavorite = manga.isFavorite == true,
                        manga = manga,
                    )
            }
        }
    }

    private fun updateMangaFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            updateMangaFavoriteUseCase(mangaId, isFavorite)
        }
    }
}
