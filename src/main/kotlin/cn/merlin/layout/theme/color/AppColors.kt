package cn.merlin.layout.theme.color

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Stable
class AppColors(
    topBar: Color,
    primary: Color,
    onPrimary: Color,
    onPrimaryStable: Color,
    secondary: Color,
    onSecondary: Color
){
    var topBar: Color by mutableStateOf(topBar)
        internal set
    var primary: Color by mutableStateOf(primary)
        internal set
    var onPrimary: Color by mutableStateOf(onPrimary)
        internal set
    var onPrimaryStable: Color by mutableStateOf(onPrimaryStable)
        internal set
    var secondary: Color by mutableStateOf(secondary)
        internal set
    var onSecondary: Color by mutableStateOf(onSecondary)
        internal set
}
