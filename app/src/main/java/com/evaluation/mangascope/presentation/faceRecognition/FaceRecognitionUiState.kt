package com.evaluation.mangascope.presentation.faceRecognition

import androidx.camera.core.SurfaceRequest
import com.evaluation.mangascope.core.UiText
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult

data class FaceRecognitionUiState(
    val faceDetectorResult: FaceDetectorResult? = null,
    val imageWidth: Int = 0,
    val imageHeight: Int = 0,
    val surfaceRequest: SurfaceRequest? = null,
    val userMessage: UiText? = null,
)
