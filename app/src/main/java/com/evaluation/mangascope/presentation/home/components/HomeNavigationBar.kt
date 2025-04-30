package com.evaluation.mangascope.presentation.home.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.evaluation.mangascope.navigation.homeRoutes

@Composable
fun HomeNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        homeRoutes().forEach { homeRoute ->
            val selected =
                currentDestination?.hierarchy?.any { it.hasRoute(homeRoute.route::class) } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(homeRoute.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) homeRoute.selectedIcon else homeRoute.unselectedIcon,
                        contentDescription = stringResource(id = homeRoute.labelId),
                    )
                },
                label = {
                    Text(text = stringResource(id = homeRoute.labelId))
                },
            )
        }
    }
}
