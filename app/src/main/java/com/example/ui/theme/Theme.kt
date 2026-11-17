package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val FreshLightColorScheme = lightColorScheme(
    primary = PrimaryEmerald,
    onPrimary = Color.White,
    primaryContainer = PrimaryFixed,
    onPrimaryContainer = PrimaryDeepForest,
    secondary = SecondaryEmerald,
    onSecondary = Color.White,
    secondaryContainer = SurfaceSubtleMint,
    onSecondaryContainer = OnSecondaryContainerForest,
    tertiary = TertiaryRuby,
    onTertiary = Color.White,
    tertiaryContainer = TertiaryContainerRuby,
    onTertiaryContainer = OnTertiaryRuby,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorContainerLight,
    background = SurfaceCanvas,
    onBackground = TextOnSurface,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceContainerLow,
    onSurfaceVariant = TextSecondary,
    outline = OutlineMuted,
    outlineVariant = BorderCard,
)

val FreshMartShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp),
)

@Composable
fun FreshMartTheme(
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = true
                controller.isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = FreshLightColorScheme,
        typography = FreshMartTypography,
        shapes = FreshMartShapes,
        content = content,
    )
}
