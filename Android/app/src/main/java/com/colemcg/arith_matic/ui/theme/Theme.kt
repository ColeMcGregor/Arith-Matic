package com.colemcg.arith_matic.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


/**
 * The central theming function for the ArithMatic app.
 * Applies the core Material Design 3 theme for the Arith-Matic app,
 *  based on a classroom-inspired color palette.
 *
 *  This function wraps the entire Compose UI in a [MaterialTheme],
 *  providing consistent theming across screens and components.
 *  It dynamically switches between light and dark color schemes
 *  based on system preferences and user settings.
 *
 * @param darkTheme Whether to use the dark color scheme. Defaults to the system setting.
 * @param dynamicColor If true, enables Material You dynamic color theming on supported devices (Android 12+).
 * @param content The composable content to which the theme will be applied.
 *
 * @author Jardina Gomez
 */
// Light theme using your classroom palette
private val LightColorScheme = lightColorScheme(
    primary = ChalkboardGreen,
    secondary = AppleRed,
    tertiary = AnswerTeal,
    background = PureWhite,
    surface = ClassroomWallYellow,
    onPrimary = PureWhite,
    onSecondary = PureWhite,
    onTertiary = PureBlack,
    onBackground = PureBlack,
    onSurface = PureBlack
)

// Dark theme using adjusted versions of your classroom palette
private val DarkColorScheme = darkColorScheme(
    primary = ClassroomWallYellow,
    secondary = AppleRed,
    tertiary = AnswerTeal,
    background = ChalkboardGreen,
    surface = ChairWoodLight,
    onPrimary = PureBlack,
    onSecondary = PureWhite,
    onTertiary = PureWhite,
    onBackground = PureWhite,
    onSurface = PureBlack
)

@Composable
fun ArithMaticTheme(

    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    // If you want to support dynamic colors on Android 12+
    val colorScheme = when {
        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // need to make type file
        shapes = AppShapes,        // need to make shapes file
        content = content
    )
}
