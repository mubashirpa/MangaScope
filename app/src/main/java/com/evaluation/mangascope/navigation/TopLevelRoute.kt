package com.evaluation.mangascope.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.evaluation.mangascope.R

data class TopLevelRoute<T : Route>(
    @StringRes val labelId: Int,
    val route: T,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Composable
fun homeRoutes() =
    listOf(
        TopLevelRoute(
            labelId = R.string.label_manga_screen,
            route = Route.Manga,
            selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
            unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook,
        ),
        TopLevelRoute(
            labelId = R.string.label_face_recognition_screen,
            route = Route.FaceRecognition,
            selectedIcon = ImageVector.vectorResource(R.drawable.baseline_ar_on_you_24),
            unselectedIcon = ImageVector.vectorResource(R.drawable.outline_ar_on_you_24),
        ),
    )
