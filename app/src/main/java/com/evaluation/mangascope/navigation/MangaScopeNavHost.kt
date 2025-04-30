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
import com.evaluation.mangascope.presentation.signIn.SignInScreen

@Composable
fun MangaScopeNavHost(
    navController: NavHostController,
    startDestination: Route,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<Route.SignIn> {
            SignInScreen(
                onSignInComplete = {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.SignIn) { inclusive = true }
                    }
                },
            )
        }
        composable<Route.Home> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Coming Soon")
            }
        }
    }
}
