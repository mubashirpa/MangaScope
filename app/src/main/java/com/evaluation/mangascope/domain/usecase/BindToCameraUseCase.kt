package com.evaluation.mangascope.domain.usecase

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.UseCase
import androidx.lifecycle.LifecycleOwner
import com.evaluation.mangascope.domain.repository.CameraRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BindToCameraUseCase(
    private val cameraRepository: CameraRepository,
) {
    operator fun invoke(
        lifecycleOwner: LifecycleOwner,
        cameraPreviewUseCase: UseCase,
        imageAnalysis: ImageAnalysis,
    ): Flow<Result<Boolean>> =
        flow {
            try {
                cameraRepository.bindToCamera(lifecycleOwner, cameraPreviewUseCase, imageAnalysis)
                emit(Result.success(true))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
}
