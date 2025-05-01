package com.evaluation.mangascope.presentation.home

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.evaluation.mangascope.navigation.HomeNavHost
import com.evaluation.mangascope.presentation.home.components.HomeNavigationBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    // Separate NavHostController for nested navigation
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            HomeNavigationBar(navController = navController)
        },
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom),
    ) { innerPadding ->
        HomeNavHost(
            navController = navController,
            modifier =
                Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
        )
    }
}
