package com.evaluation.mangascope.presentation.manga

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.evaluation.mangascope.R
import com.evaluation.mangascope.presentation.components.ErrorScreen
import com.evaluation.mangascope.presentation.components.LoadingScreen
import com.evaluation.mangascope.presentation.manga.components.MangaListItem
import com.evaluation.mangascope.presentation.utils.header

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaScreen(
    uiState: MangaUiState,
    onNavigateToMangaDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyPagingItems = uiState.manga.collectAsLazyPagingItems()

    Scaffold(modifier = modifier) { innerPadding ->
        when (lazyPagingItems.loadState.refresh) {
            is LoadState.Error -> {
                val message = stringResource(R.string.error_unknown)
                ErrorScreen(
                    onRetryClick = {
                        lazyPagingItems.refresh()
                    },
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    errorMessage = message,
                )
            }

            LoadState.Loading -> {
                LoadingScreen(modifier = Modifier.padding(innerPadding))
            }

            is LoadState.NotLoading -> {
                val layoutDirection = LocalLayoutDirection.current
                val contentPadding =
                    PaddingValues(
                        top = 12.dp,
                        bottom = 12.dp + innerPadding.calculateBottomPadding(),
                        start = 16.dp + innerPadding.calculateStartPadding(layoutDirection),
                        end = 16.dp + innerPadding.calculateEndPadding(layoutDirection),
                    )

                PullToRefreshBox(
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = {
                        lazyPagingItems.refresh()
                    },
                    modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(100.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = contentPadding,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(
                            count = lazyPagingItems.itemCount,
                            key = lazyPagingItems.itemKey(),
                            contentType = lazyPagingItems.itemContentType(),
                        ) { index ->
                            lazyPagingItems[index]?.let { manga ->
                                MangaListItem(
                                    onClick = {
                                        manga.id?.let(onNavigateToMangaDetails)
                                    },
                                    thumb = manga.thumb.orEmpty(),
                                    modifier = Modifier.animateItem(),
                                )
                            }
                        }

                        when (lazyPagingItems.loadState.append) {
                            is LoadState.Error -> {
                                header {
                                    val message = stringResource(R.string.error_unknown)
                                    ErrorScreen(
                                        onRetryClick = {
                                            lazyPagingItems.retry()
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        errorMessage = message,
                                    )
                                }
                            }

                            LoadState.Loading -> {
                                header {
                                    LoadingScreen()
                                }
                            }

                            is LoadState.NotLoading -> Unit
                        }
                    }
                }
            }
        }
    }
}
