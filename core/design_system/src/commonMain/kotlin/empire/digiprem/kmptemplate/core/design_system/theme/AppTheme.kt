package empire.digiprem.kmptemplate.core.design_system.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

private val LightColorScheme = lightColorScheme(
    primary          = Primary,
    secondary        = Secondary,
    background       = Background,
    surface          = Surface,
    onPrimary        = Surface,
    onSecondary      = Surface,
    onBackground     = OnSurface,
    onSurface        = OnSurface,
    error            = Error,
)

private val DarkColorScheme = darkColorScheme(
    primary          = PrimaryLight,
    secondary        = Secondary,
    background       = BackgroundDark,
    surface          = SurfaceDark,
    onPrimary        = SurfaceDark,
    onSecondary      = SurfaceDark,
    onBackground     = OnSurfaceDark,
    onSurface        = OnSurfaceDark,
    error            = Error,
)

val LocalColorScheme = staticCompositionLocalOf<ColorScheme> {
    error("No ColorScheme provided")
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalColorScheme provides colorScheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = AppTypography(),
            content     = content,
        )
    }
}
