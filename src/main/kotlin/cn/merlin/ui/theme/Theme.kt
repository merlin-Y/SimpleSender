package cn.merlin.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import cn.merlin.tools.changeTheme
import com.github.tkuenneth.nativeparameterstoreaccess.Dconf
import com.github.tkuenneth.nativeparameterstoreaccess.Dconf.HAS_DCONF
import com.github.tkuenneth.nativeparameterstoreaccess.MacOSDefaults
import com.github.tkuenneth.nativeparameterstoreaccess.NativeParameterStoreAccess.IS_MACOS
import com.github.tkuenneth.nativeparameterstoreaccess.NativeParameterStoreAccess.IS_WINDOWS
import com.github.tkuenneth.nativeparameterstoreaccess.WindowsRegistry

const val TWEEN_DURATION = 500
val isInDarkMode = mutableStateOf(true)

private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_errorContainer,
    onErrorContainer = md_theme_light_onErrorContainer,
    outline = md_theme_light_outline,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    inverseSurface = md_theme_light_inverseSurface,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)

private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onErrorContainer = md_theme_dark_onErrorContainer,
    outline = md_theme_dark_outline,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    inverseSurface = md_theme_dark_inverseSurface,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)


@Composable
fun MainTheme(
    content: @Composable ()->Unit
){
    val useDarkTheme = remember{ mutableStateOf(isSystemInDarkTheme())}

    useDarkTheme.value = when(changeTheme.value){
        0 -> isInDarkMode.value
        1 -> false
        2 -> true
        else -> isInDarkMode.value
    }

    val targetColors = if(useDarkTheme.value) DarkColors else LightColors

    val primary = animateColorAsState(targetColors.primary,TweenSpec(TWEEN_DURATION))
    val onPrimary = animateColorAsState(targetColors.onPrimary, TweenSpec(TWEEN_DURATION))
    val primaryContainer = animateColorAsState(targetColors.primaryContainer,TweenSpec(TWEEN_DURATION))
    val onPrimaryContainer = animateColorAsState(targetColors.onPrimaryContainer,TweenSpec(TWEEN_DURATION))
    val secondary = animateColorAsState(targetColors.secondary, TweenSpec(TWEEN_DURATION))
    val onSecondary = animateColorAsState(targetColors.onSecondary, TweenSpec(TWEEN_DURATION))
    val secondaryContainer = animateColorAsState(targetColors.secondaryContainer,TweenSpec(TWEEN_DURATION))
    val onSecondaryContainer = animateColorAsState(targetColors.onSecondaryContainer,TweenSpec(TWEEN_DURATION))
    val tertiary = animateColorAsState(targetColors.tertiary,TweenSpec(TWEEN_DURATION))
    val onTertiary = animateColorAsState(targetColors.onTertiary,TweenSpec(TWEEN_DURATION))

    val appColor by mutableStateOf(
        ColorScheme(
            primary = primary.value,
            onPrimary = onPrimary.value,
            primaryContainer = primaryContainer.value,
            onPrimaryContainer = onPrimaryContainer.value,
            secondary = secondary.value,
            onSecondary = onSecondary.value,
            secondaryContainer = secondaryContainer.value,
            onSecondaryContainer = onSecondaryContainer.value,
            tertiary = tertiary.value,
            onTertiary = onTertiary.value,
            tertiaryContainer = Color(0xffffff),
            onTertiaryContainer = Color(0xffffff),
            background = Color(0xffffff),
            onBackground = Color(0xffffff),
            surface = Color(0xffffff),
            surfaceTint = Color(0xffffff),
            surfaceVariant = Color(0xffffff),
            onSurfaceVariant = Color(0xffffff),
            onSurface = Color(0xffffff),
            error = Color(0xffffff),
            onError = Color(0xffffff),
            errorContainer = Color(0xffffff),
            onErrorContainer = Color(0xffffff),
            outline = Color(0xffffff),
            outlineVariant = Color(0xffffff),
            scrim = Color(0xffffff),
            inversePrimary = Color(0xffffff),
            inverseSurface = Color(0xffffff),
            inverseOnSurface = Color(0xffffff)
        )
    )

        MaterialTheme(
            colorScheme = appColor,
            content = content,
            shapes = Shapes
        )
    }

fun isSystemInDarkTheme(): Boolean = when {
    IS_WINDOWS -> {
        val result = WindowsRegistry.getWindowsRegistryEntry(
            "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
            "AppsUseLightTheme")
        result == 0x0
    }
    IS_MACOS -> {
        val result = MacOSDefaults.getDefaultsEntry("AppleInterfaceStyle")
        result == "Dark"
    }
    HAS_DCONF -> {
        val result = Dconf.getDconfEntry("/org/gnome/desktop/interface/gtk-theme")
        result.lowercase().contains("dark")
    }
    else -> false
}