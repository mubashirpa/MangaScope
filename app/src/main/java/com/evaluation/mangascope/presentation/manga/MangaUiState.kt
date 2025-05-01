package com.evaluation.mangascope.presentation.manga

import androidx.paging.PagingData
import com.evaluation.mangascope.domain.model.manga.Manga
import kotlinx.coroutines.flow.MutableStateFlow

data class MangaUiState(
    val manga: MutableStateFlow<PagingData<Manga>> = MutableStateFlow(PagingData.empty()),
    val isRefreshing: Boolean = false,
)
