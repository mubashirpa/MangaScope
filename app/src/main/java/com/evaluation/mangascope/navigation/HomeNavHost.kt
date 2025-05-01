package com.evaluation.mangascope.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.evaluation.mangascope.presentation.manga.MangaScreen
import com.evaluation.mangascope.presentation.manga.MangaViewModel
import com.evaluation.mangascope.presentation.mangaDetails.MangaDetailsScreen
import com.evaluation.mangascope.presentation.mangaDetails.MangaDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Route.Manga,
        modifier = modifier,
    ) {
        composable<Route.Manga> {
            val viewModel: MangaViewModel = koinViewModel()
            MangaScreen(
                uiState = viewModel.uiState,
                onNavigateToMangaDetails = { mangaId ->
                    navController.navigate(Route.MangaDetails(mangaId))
                },
            )
        }
        composable<Route.FaceRecognition> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Coming Soon")
            }
        }
        composable<Route.MangaDetails> {
            val viewModel: MangaDetailsViewModel = koinViewModel()
            MangaDetailsScreen(
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
            )
        }
    }
}
