package com.evaluation.mangascope.presentation.home

import androidx.compose.foundation.layout.padding
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
    ) { innerPadding ->
        HomeNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}
