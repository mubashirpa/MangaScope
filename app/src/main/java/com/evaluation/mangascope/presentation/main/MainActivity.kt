package com.evaluation.mangascope.presentation.main

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.evaluation.mangascope.navigation.MangaScopeNavHost
import com.evaluation.mangascope.navigation.Route
import com.evaluation.mangascope.presentation.components.LoadingScreen
import com.evaluation.mangascope.presentation.theme.MangaScopeTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { viewModel.uiState.isLoading }

        enableEdgeToEdge(
            statusBarStyle =
                SystemBarStyle.auto(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                    detectDarkMode = { true },
                ),
        )

        setContent {
            MangaScopeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0),
                ) { innerPadding ->
                    MangaScopeApplication(
                        uiState = viewModel.uiState,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
private fun MangaScopeApplication(
    uiState: MainUiState,
    modifier: Modifier = Modifier,
) {
    if (uiState.isLoading) {
        LoadingScreen(modifier = modifier)
    } else {
        MangaScopeNavHost(
            navController = rememberNavController(),
            startDestination = if (uiState.isUserSignedIn) Route.Home else Route.SignIn,
            modifier = modifier,
        )
    }
}
