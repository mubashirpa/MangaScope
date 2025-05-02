package com.evaluation.mangascope.domain.repository

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.UseCase
import androidx.lifecycle.LifecycleOwner

interface CameraRepository {
    suspend fun bindToCamera(
        lifecycleOwner: LifecycleOwner,
        cameraPreviewUseCase: UseCase,
        imageAnalysis: ImageAnalysis,
    )
}
