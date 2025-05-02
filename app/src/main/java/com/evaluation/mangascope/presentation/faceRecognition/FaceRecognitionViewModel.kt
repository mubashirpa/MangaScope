package com.evaluation.mangascope.presentation.faceRecognition

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evaluation.mangascope.core.FaceDetectorHelper
import com.evaluation.mangascope.core.UiText
import com.evaluation.mangascope.domain.usecase.BindToCameraUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class FaceRecognitionViewModel(
    private val faceDetectorHelper: FaceDetectorHelper,
    private val bindToCameraUseCase: BindToCameraUseCase,
) : ViewModel(),
    FaceDetectorHelper.DetectorListener {
    var uiState by mutableStateOf(FaceRecognitionUiState())
        private set

    private lateinit var cameraPreview: Preview
    private lateinit var imageAnalysis: ImageAnalysis

    init {
        faceDetectorHelper.faceDetectorListener = this
    }

    override fun onError(
        error: String,
        errorCode: Int,
    ) {
        uiState = uiState.copy(userMessage = UiText.DynamicString(error))
    }

    override fun onResults(result: FaceDetectorHelper.ResultBundle) {
        val faceDetectorResult = result.results.firstOrNull()
        val faceDetected by
            derivedStateOf {
                (faceDetectorResult?.detections()?.size ?: 0) > 0
            }

        uiState =
            uiState.copy(
                faceDetected = faceDetected,
                faceDetectorResult = faceDetectorResult,
                imageWidth = result.inputImageWidth,
                imageHeight = result.inputImageHeight,
            )
    }

    fun onEvent(event: FaceRecognitionUiEvent) {
        when (event) {
            is FaceRecognitionUiEvent.BindToCamera -> {
                bindCameraUseCases(event.lifecycleOwner)
            }

            FaceRecognitionUiEvent.OnPause -> {
                viewModelScope.launch(Dispatchers.Default) {
                    faceDetectorHelper.closeFaceDetector()
                }
            }

            FaceRecognitionUiEvent.OnResume -> {
                viewModelScope.launch(Dispatchers.Default) {
                    if (faceDetectorHelper.isClosed()) {
                        faceDetectorHelper.initFaceDetector()
                    }
                }
            }

            FaceRecognitionUiEvent.UserMessageShown -> {
                uiState = uiState.copy(userMessage = null)
            }
        }
    }

    private fun bindCameraUseCases(lifecycleOwner: LifecycleOwner) {
        cameraPreview =
            Preview
                .Builder()
                .setResolutionSelector(
                    ResolutionSelector
                        .Builder()
                        .setAspectRatioStrategy(AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY)
                        .build(),
                )
                // .setTargetRotation(previewView.display.rotation)
                .build()
                .apply {
                    setSurfaceProvider { surfaceRequest ->
                        uiState = uiState.copy(surfaceRequest = surfaceRequest)
                    }
                }

        imageAnalysis =
            ImageAnalysis
                .Builder()
                .setResolutionSelector(
                    ResolutionSelector
                        .Builder()
                        .setAspectRatioStrategy(AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY)
                        .build(),
                )
                // .setTargetRotation(previewView.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(
                        Executors.newSingleThreadExecutor(),
                        faceDetectorHelper::detectLivestreamFrame,
                    )
                }

        bindToCameraUseCase(lifecycleOwner, cameraPreview, imageAnalysis)
            .onEach { result ->
                result.onFailure {
                    uiState =
                        uiState.copy(userMessage = UiText.DynamicString(it.message.toString()))
                }
            }.launchIn(viewModelScope)
    }
}
