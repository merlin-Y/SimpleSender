package cn.merlin.layout.leftMenu

import androidx.compose.animation.*
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.layout.theme.TWEEN_DURATION
import cn.merlin.layout.topbar.isMenuBarPickUp
import org.succlz123.lib.imageloader.ImageRes
import org.succlz123.lib.imageloader.core.ImageCallback

@Composable
fun leftMenuBar(
    width: Dp,
){
    val leftButtomMenuHeight = animateDpAsState(if(!isMenuBarPickUp.value) 455.dp else 500.dp,TweenSpec(300))

    Surface(
        modifier = Modifier.size(width, 700.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column() {
            val density = LocalDensity.current
            AnimatedVisibility(
                visible = !isMenuBarPickUp.value,
                enter = slideInVertically() {
                    with(density) { -40.dp.roundToPx() }
                } + expandVertically (
                    expandFrom = Alignment.Top
                ) + fadeIn(
                    initialAlpha = 0.3f
                ),
                exit = slideOutHorizontally() + shrinkVertically() + fadeOut()
            ) {
                Column{
                    Text(
                        modifier = Modifier.size(width,40.dp).padding(start = 20.dp),
                        text = "设备",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 16.sp,
                        maxLines = 1,
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.background(MaterialTheme.colorScheme.tertiary).height(2.dp).width(width))
                }
            }
            LazyColumn (
                modifier = Modifier.size(width,leftButtomMenuHeight.value)
            ){

            }
            Spacer(modifier = Modifier.background(MaterialTheme.colorScheme.tertiary).height(2.dp).width(width))
            Column(
                modifier = Modifier.size(width = width, height = 700.dp - leftButtomMenuHeight.value),
                horizontalAlignment = Alignment.CenterHorizontally){
                MenuItem("Icons/history.png","发送历史")
                MenuItem("Icons/search.png","搜索设备")
                MenuItem("Icons/settings.webp","系统设置")
            }

        }
    }
}

@Composable
fun MenuItem(
    Icon: String,
    text: String
){
    Button(
        modifier = Modifier.padding(0.dp),
        onClick = {}
    ){
        Row {
            ImageRes(Icon,
                imageCallback = ImageCallback {
                    Image(modifier = Modifier.size(30.dp), painter = it, contentDescription = "res")
                })
            val density = LocalDensity.current
            AnimatedVisibility(
                visible = !isMenuBarPickUp.value,
                enter = scaleIn() ,
                exit = scaleOut() + fadeOut()
            ) {
                Text(text = text, modifier = Modifier.padding(start = 10.dp))
            }
        }
    }
}