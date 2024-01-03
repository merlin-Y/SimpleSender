package cn.merlin.layout.leftMenu

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.bean.model.MenuItemModel
import cn.merlin.layout.checkdevice.DetectDevice
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import moe.tlaster.precompose.ui.LocalViewModelStoreOwner
import moe.tlaster.precompose.ui.viewModel

object NavigatorManager {
    lateinit var navigator: Navigator
}

@Composable
fun MainLeftMenu(
    width: Dp,
    height: Dp
){
    val navigator = NavigatorManager.navigator
    val viewModel: MenuItemModel = viewModel { MenuItemModel() }
    NavHost(
        modifier = Modifier.size(width,height),
        navigator = navigator,
        navTransition = remember {
            NavTransition(
                createTransition = fadeIn(),
                destroyTransition = fadeOut(),
                pauseTransition = fadeOut(),
                resumeTransition = fadeIn(),
            )
        }, initialRoute = "DetectDevice"
    ){
        scene(route = "DetectDevice", navTransition = NavTransition()){
            DetectDevice(900.dp - width)
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .size(width,height)
            .background(MaterialTheme.colorScheme.primary)
    ){
        MenuItem(viewModel,"DetectDevice","Icons/search.png"){
            navigator.navigate("DetectDevice", NavOptions(launchSingleTop = true))
        }
    }
}

@Composable
fun MenuItem(
    viewModel: MenuItemModel,
    title: String,
    Icon: String,
    onClick: (title: Any) -> Unit
){
    Row (
        modifier = Modifier.onClick {
            viewModel.selectedMenuTag.value = title
            onClick(title)
        }.let{
            if(viewModel.selectedMenuTag.value == title){
                it.background(MaterialTheme.colorScheme.secondary)
            }
         else { it }
        },
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(painterResource(Icon),"", modifier = Modifier.size(18.dp))
        Text(text = title, modifier = Modifier.padding(horizontal = 6.dp), fontSize = 12.sp, maxLines = 1, color = MaterialTheme.colorScheme.onPrimary, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun Modifier.onClick(enableRipple: Boolean = false, rippleColor: Color = Color.Unspecified, onClick: () -> Unit) = this.clickable (
    interactionSource = remember { MutableInteractionSource() },
    indication = if (enableRipple) rememberRipple(color = rippleColor, bounded = true) else null
) {
    onClick()
}

