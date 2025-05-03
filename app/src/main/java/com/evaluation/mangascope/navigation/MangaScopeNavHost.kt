package com.evaluation.mangascope.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.evaluation.mangascope.presentation.home.HomeScreen
import com.evaluation.mangascope.presentation.signIn.SignInScreen

@Composable
fun MangaScopeNavHost(
    navController: NavHostController,
    startDestination: Route,
    modifier: Modifier = Modifier,
) {
    val activity = LocalActivity.current
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<Route.SignIn> {
            SignInScreen(
                onNavigateUp = {
                    activity?.finish()
                },
                onSignInComplete = {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.SignIn) { inclusive = true }
                    }
                },
            )
        }
        composable<Route.Home> {
            HomeScreen()
        }
    }
}
