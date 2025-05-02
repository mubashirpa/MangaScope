package com.evaluation.mangascope.presentation.faceRecognition

import androidx.camera.compose.CameraXViewfinder
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.evaluation.mangascope.OverlayView

@Composable
fun FaceRecognitionScreen(
    uiState: FaceRecognitionUiState,
    onEvent: (FaceRecognitionUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val overlayView = remember { OverlayView(context, null) }
    val snackbarHostState = remember { SnackbarHostState() }

    uiState.userMessage?.let { userMessage ->
        LaunchedEffect(userMessage) {
            snackbarHostState.showSnackbar(userMessage.asString(context))
            // Once the message is displayed and dismissed, notify the ViewModel.
            onEvent(FaceRecognitionUiEvent.UserMessageShown)
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            uiState.surfaceRequest?.let { surfaceRequest ->
                CameraXViewfinder(
                    surfaceRequest = surfaceRequest,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(0.75f)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.shapes.medium,
                        ),
            ) {
                AndroidView(
                    factory = { overlayView },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        onEvent(FaceRecognitionUiEvent.BindToCamera(lifecycleOwner))

        val observer =
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        onEvent(FaceRecognitionUiEvent.OnResume)
                    }

                    Lifecycle.Event.ON_PAUSE -> {
                        onEvent(FaceRecognitionUiEvent.OnPause)
                    }

                    else -> Unit
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(uiState.faceDetectorResult) {
        if (uiState.faceDetectorResult != null) {
            // Pass necessary information to OverlayView for drawing on the canvas
            overlayView.setResults(
                uiState.faceDetectorResult,
                uiState.imageWidth,
                uiState.imageHeight,
            )

            // Force a redraw
            overlayView.invalidate()
        }
    }
}
