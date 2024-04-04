package cn.merlin.layout.leftMenu

import androidx.compose.animation.*
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.bean.model.DeviceModel
import cn.merlin.layout.mainWindow.*
import cn.merlin.layout.theme.TWEEN_DURATION
import cn.merlin.layout.topbar.isMenuBarPickUp
import cn.merlin.network.Sender
import kotlinx.coroutines.*
import moe.tlaster.precompose.navigation.Navigator
import java.io.File

val sender = Sender()

@Composable
fun leftMenuBar(
    localDeviceList: SnapshotStateList<DeviceModel>,
    width: Dp,
    navigator: Navigator
) {
    val leftButtonMenuHeight = animateDpAsState(if (!isMenuBarPickUp.value) 455.dp else 500.dp, TweenSpec(300))

    Surface(
        modifier = Modifier.size(width, 700.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column{
            val density = LocalDensity.current
            AnimatedVisibility(
                visible = !isMenuBarPickUp.value,
                enter = slideInVertically{
                    with(density) { -40.dp.roundToPx() }
                } + expandVertically(
                    expandFrom = Alignment.Top
                ) + fadeIn(
                    initialAlpha = 0.3f
                ),
                exit = slideOutHorizontally() + shrinkVertically() + fadeOut()
            ) {
                Column {
                    Text(
                        modifier = Modifier.size(width, 40.dp).padding(start = 20.dp),
                        text = "设备",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 16.sp,
                        maxLines = 1,
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.background(MaterialTheme.colorScheme.tertiary).height(2.dp).width(width))
                }
            }
            LazyColumn(
                modifier = Modifier
                    .size(width, leftButtonMenuHeight.value)
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                localDeviceList.forEach{
                    item {
                        ListItem(it,navigator)
                    }
                }
            }
            Spacer(modifier = Modifier.background(MaterialTheme.colorScheme.tertiary).height(2.dp).width(width))
            Column(
                modifier = Modifier.size(width = width, height = 245.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MenuItem("Icons/search.webp", "搜索设备",navigator)
                MenuItem("Icons/history.webp", "发送历史",navigator)
                MenuItem("Icons/settings.webp", "系统设置",navigator)
            }

        }
    }
}

@Composable
fun MenuItem(
    icon: String,
    text: String,
    navigator: Navigator
) {
    val imageWidth = animateDpAsState(if (isMenuBarPickUp.value) 20.dp else 30.dp, TweenSpec(200))
    val textWidth =
        animateDpAsState(if (isMenuBarPickUp.value) 0.dp else 60.dp, TweenSpec(durationMillis = 200, delay = 200))

    FilledIconButton(
        modifier = Modifier.padding(0.dp).fillMaxWidth().height(52.dp),
        shape = MaterialTheme.shapes.extraSmall,
        onClick = {
            when (text) {
                "搜索设备" -> {
                    navigator.navigate("/detect")
                    sender.detectDevices()
                }
                "系统设置" -> {
                    navigator.navigate("/settings")
                }
                else -> {
                    navigator.navigate("/history")
                }
            }
        }
    ) {
        Row {
            Image(
                painter = painterResource(icon),
                contentDescription = text,
                modifier = Modifier.size(imageWidth.value)
            )
            AnimatedVisibility(
                visible = !isMenuBarPickUp.value,
                enter = scaleIn(TweenSpec(durationMillis = 200, delay = 200)),
                exit = scaleOut(TweenSpec(durationMillis = 200, delay = 200)) + fadeOut(TweenSpec(100))
            ) {
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 10.dp).width(textWidth.value),
                    fontSize = 14.sp,
                    maxLines = 1
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ListItem(
    deviceModel: DeviceModel,
    navigator: Navigator,
) {
    val text = if(deviceModel.deviceNickName.value != "") deviceModel.deviceNickName.value else deviceModel.deviceName.value
    val scrollState = rememberScrollState(0)
    val scrollStateCoroutine = rememberCoroutineScope()
    val imageWidth = animateDpAsState(if (isMenuBarPickUp.value) 20.dp else 30.dp, TweenSpec(TWEEN_DURATION))
    val textWidth =
        animateDpAsState(if (isMenuBarPickUp.value) 0.dp else 80.dp, TweenSpec(durationMillis = 200, delay = 200))
    val buttonColor = if(deviceModel.inListType.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary

    FilledIconButton(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .height(52.dp)
            .onPointerEvent(PointerEventType.Enter) {
                scrollStateCoroutine.launch {
                    if (!isMenuBarPickUp.value) {
                        scrollState.animateScrollTo(scrollState.maxValue, tween(text.length * 200))
                    }
                }
            }
            .onPointerEvent(PointerEventType.Exit) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (!isMenuBarPickUp.value) {
                        scrollState.scrollTo(0)
                    }
                }
            },
        colors = IconButtonDefaults.iconButtonColors(containerColor = animateColorAsState(buttonColor, TweenSpec(TWEEN_DURATION)).value),
        shape = MaterialTheme.shapes.extraSmall,
        onClick = {

            navigator.navigate("/message")
            currentDevice.value = deviceModel
        }
    ) {
        Row {
            Image(
                painter = painterResource(
                    when (deviceModel.deviceType.value) {
                        "computer" -> "Icons/computer.png"
                        "phone" -> "Icons/phone.png"
                        "laptop" -> "Icons/laptop.png"
                        else -> "Icons/phone.png"
                    }
                ),
                contentDescription = text,
                modifier = Modifier.size(imageWidth.value)
            )
            AnimatedVisibility(
                visible = !isMenuBarPickUp.value,
                enter = scaleIn(TweenSpec(durationMillis = 200, delay = 200)),
                exit = scaleOut(TweenSpec(durationMillis = 200, delay = 200)) + fadeOut(TweenSpec(100))
            ) {
                Text(
                    text = text,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .width(textWidth.value)
                        .horizontalScroll(scrollState),
                    fontSize = 14.sp,
                    maxLines = 1
                )
            }
        }
    }
}