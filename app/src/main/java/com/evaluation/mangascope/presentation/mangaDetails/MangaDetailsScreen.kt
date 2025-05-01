package com.evaluation.mangascope.presentation.mangaDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.evaluation.mangascope.domain.model.manga.Manga
import com.evaluation.mangascope.presentation.theme.MangaScopeTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MangaDetailsScreen(
    uiState: MangaDetailsUiState,
    onEvent: (MangaDetailsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconToggleButton(
                        checked = uiState.isFavorite,
                        onCheckedChange = {
                            onEvent(MangaDetailsUiEvent.OnMangaFavoriteChange(it))
                        },
                    ) {
                        if (uiState.isFavorite) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.StarOutline,
                                contentDescription = null,
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            uiState.manga?.let { manga ->
                Header(manga)
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = manga.summary.orEmpty(),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun Header(manga: Manga) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        Card {
            AsyncImage(
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(manga.thumb)
                        .crossfade(true)
                        .build(),
                contentDescription = null,
                modifier =
                    Modifier
                        .width(100.dp)
                        .aspectRatio(2F / 3F),
                contentScale = ContentScale.FillBounds,
            )
        }
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = manga.title?.trim().orEmpty(),
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = manga.subTitle.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MangaDetailsScreenPreview(
    @PreviewParameter(MangaPreviewParameterProvider::class) manga: Manga,
) {
    MangaScopeTheme {
        MangaDetailsScreen(
            uiState =
                MangaDetailsUiState(
                    manga = manga,
                    isFavorite = true,
                ),
            onEvent = {},
        )
    }
}

class MangaPreviewParameterProvider : PreviewParameterProvider<Manga> {
    override val values =
        sequenceOf(
            Manga(
                subTitle = LoremIpsum(10).values.first(),
                summary = LoremIpsum(20).values.first(),
                title = LoremIpsum(3).values.first(),
            ),
        )
}
