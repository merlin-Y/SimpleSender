package cn.merlin.layout.leftMenu

import androidx.compose.animation.*
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.bean.model.MenuItemModel
import cn.merlin.layout.checkdevice.DetectDevice
import cn.merlin.layout.theme.TWEEN_DURATION
import cn.merlin.layout.topbar.isMenuBarPickUp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import moe.tlaster.precompose.ui.LocalViewModelStoreOwner
import moe.tlaster.precompose.ui.viewModel

@Composable
fun MainLeftMenu(
    navigator: Navigator,
    width: Dp,
    height: Dp
){
    val leftButtomMenuHeight = animateDpAsState(if(!isMenuBarPickUp.value) 455.dp else 500.dp,TweenSpec(300))

    NavHost(
        modifier = Modifier.size(width,height),
        navigator = navigator,
        navTransition = NavTransition(),
        initialRoute = "DetectDevice"
    ){
        scene(route = "DetectDevice", navTransition = NavTransition()){
            DetectDevice(900.dp - width)
        }
    }

//    Surface(
//        modifier = Modifier.size(width, 700.dp),
//        color = MaterialTheme.colorScheme.primary
//    ) {
//        Column() {
//            val density = LocalDensity.current
//            AnimatedVisibility(
//                visible = !isMenuBarPickUp.value,
//                enter = slideInVertically() {
//                    with(density) { -40.dp.roundToPx() }
//                } + expandVertically (
//                    expandFrom = Alignment.Top
//                ) + fadeIn(
//                    initialAlpha = 0.3f
//                ),
//                exit = slideOutHorizontally() + shrinkVertically() + fadeOut()
//            ) {
//                Column{
//                    Text(
//                        modifier = Modifier.size(width,40.dp).padding(start = 20.dp),
//                        text = "设备",
//                        color = MaterialTheme.colorScheme.onPrimaryContainer,
//                        fontSize = 16.sp,
//                        maxLines = 1,
//                        lineHeight = 32.sp
//                    )
//                    Spacer(modifier = Modifier.background(MaterialTheme.colorScheme.tertiary).height(2.dp).width(width))
//                }
//            }
//            LazyColumn (
//                modifier = Modifier.size(width,leftButtomMenuHeight.value)
//            ){
//
//            }
//            Spacer(modifier = Modifier.background(MaterialTheme.colorScheme.tertiary).height(2.dp).width(width))
//            Column(
//                modifier = Modifier.size(width = width, height = 700.dp - leftButtomMenuHeight.value),
//                horizontalAlignment = Alignment.CenterHorizontally){
//                mainMenuItem(navigator,"Icons/history.webp","发送历史","history",width)
//                mainMenuItem(navigator,"Icons/search.webp","搜索设备","search",width)
//                mainMenuItem(navigator,"Icons/settings.webp","系统设置","settings",width)
//            }
//
//        }
//    }
}

@Composable
fun mainMenuItem(
    navigator: Navigator,
    Icon: String,
    title: String,
    text: String,
    width: Dp
){
    val imageWidth = animateDpAsState(if(isMenuBarPickUp.value) 20.dp else 30.dp, TweenSpec(TWEEN_DURATION))
    val textWidth = animateDpAsState(if(isMenuBarPickUp.value) 0.dp else 60.dp,
        TweenSpec(durationMillis = 200, delay = 200)
    )

    FilledIconButton(
        modifier = Modifier.padding(0.dp).fillMaxWidth().height(52.dp),
        shape = MaterialTheme.shapes.extraSmall,
        onClick = {
            navigator.navigate(title,NavOptions(launchSingleTop = true))
        }
    ){
        Row {
//            ImageRes(Icon,
//                imageCallback = ImageCallback {
//                    Image(modifier = Modifier.size(30.dp), painter = it, contentDescription = "res")
//                })
//            val density = LocalDensity.current
            Image(painter = painterResource(Icon), contentDescription = text, modifier = Modifier.size(imageWidth.value))
//            Text(text = text, modifier = Modifier.padding(start = 10.dp).width(100.dp-imageWidth.value), fontSize = 14.sp)
            AnimatedVisibility(
                visible = !isMenuBarPickUp.value,
                enter = scaleIn(TweenSpec(durationMillis = 200, delay = 200)) ,
                exit = scaleOut(TweenSpec(durationMillis = 200, delay = 200)) + fadeOut(TweenSpec(100))
            ) {
                Text(text = text, modifier = Modifier.padding(start = 10.dp).width(textWidth.value), fontSize = 14.sp, maxLines = 1)
            }
        }
    }
}

@Composable
fun Modifier.onClick(enableRipple: Boolean = false, rippleColor: Color = Color.Unspecified, onClick: () -> Unit) = this.clickable (
    interactionSource = remember { MutableInteractionSource() },
    indication = if (enableRipple) rememberRipple(color = rippleColor, bounded = true) else null
) {
    onClick()
}

