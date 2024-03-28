package cn.merlin.layout.topbar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
//import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import kotlin.system.exitProcess

val isMenuBarPickUp = mutableStateOf(false)

@Composable
fun TittleBar(
    icon: String,
    title: String,
    windowState: WindowState
) {
    val appIconStartDp = animateDpAsState(if (isMenuBarPickUp.value) 20.dp else 0.dp)
    val offsetX = mutableStateOf(0f)
    val offsetY = mutableStateOf(0f)

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX.value += dragAmount.x
                    offsetY.value += dragAmount.y
                    windowState.position = WindowPosition(
                        windowState.position.x + offsetX.value.toDp(),
                        windowState.position.y + offsetY.value.toDp()
                    )
                }
            }
    ) {
        Row {
            FilledIconButton(
                shape = MaterialTheme.shapes.extraSmall,
                modifier = Modifier,
                onClick = {
                    isMenuBarPickUp.value = !isMenuBarPickUp.value
                }
            ) {
                Icon(
                    Icons.Filled.Menu, "",
                    modifier = Modifier.height(25.dp).width(25.dp).padding(0.dp)
                )
            }
            FilledIconButton(
                shape = MaterialTheme.shapes.extraSmall,
                modifier = Modifier.padding(start = appIconStartDp.value),
                onClick = {

                }
            ) {
                Image(painterResource(icon), "", modifier = Modifier.height(20.dp).width(20.dp).padding(0.dp))
            }
            Text(
                title,
                modifier = Modifier.width(windowState.size.width - 185.dp - appIconStartDp.value).fillMaxHeight()
                    .padding(start = 10.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp,
                lineHeight = 30.sp
            )
            FilledIconButton(
                shape = MaterialTheme.shapes.extraSmall,
                onClick = {
                    windowState.isMinimized = true
                }
            ) {
                Icon(painterResource("Icons/minimum.webp"), "", modifier = Modifier.size(18.dp))
            }
            FilledIconButton(
                shape = MaterialTheme.shapes.extraSmall,
                onClick = {
                    exitProcess(0)
                }
            ) {
                Icon(Icons.Filled.Close, "")
            }
        }
    }
}