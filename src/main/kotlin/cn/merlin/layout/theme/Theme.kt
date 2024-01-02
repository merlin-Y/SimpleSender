package cn.merlin.layout.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import cn.merlin.layout.theme.color.AppColors
import cn.merlin.layout.theme.color.palette.DarkColors
import cn.merlin.layout.theme.color.palette.LightColors
import cn.merlin.utils.checkIfContain
import java.util.prefs.Preferences

val AppColorsProvider = compositionLocalOf { LightColors }
val isUserDarkThemeOn =  mutableStateOf(false)
//val themeTypeState: MutableState<Int> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
//    mutableStateOf()
//}
const val TWEEN_DURATION = 200
@Composable
fun MainTheme(
    Settings: SnapshotStateMap<String, MutableState<Boolean>>,
    content: @Composable ()->Unit
){

    val useDarkTheme = remember { mutableStateOf(Settings.getValue("useDarkTheme").value) }
    useDarkTheme.value = isUserDarkThemeOn.value
    val targetColors = if(useDarkTheme.value) DarkColors else LightColors

    val topBar = animateColorAsState(targetColors.topBar,TweenSpec(TWEEN_DURATION))
    val primary = animateColorAsState(targetColors.primary, TweenSpec(TWEEN_DURATION))
    val onPrimary = animateColorAsState(targetColors.onPrimary,TweenSpec(TWEEN_DURATION))
    val onPrimaryStable = animateColorAsState(targetColors.onPrimaryStable,TweenSpec(TWEEN_DURATION))
    val secondary = animateColorAsState(targetColors.secondary, TweenSpec(TWEEN_DURATION))
    val onSecondary = animateColorAsState(targetColors.onSecondary, TweenSpec(TWEEN_DURATION))

    val appColors = AppColors(
        topBar = topBar.value,
        primary = primary.value,
        onPrimary = onPrimary.value,
        onPrimaryStable = onPrimaryStable.value,
        secondary = secondary.value,
        onSecondary = onSecondary.value
    )

    CompositionLocalProvider(AppColorsProvider provides appColors){
        MaterialTheme(
            content = content,
            shapes = Shapes
        )
    }
}