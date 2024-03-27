package cn.merlin.layout.leftMenu

import androidx.compose.animation.*
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.bean.Device
import cn.merlin.bean.model.DeviceModel
import cn.merlin.layout.theme.TWEEN_DURATION
import cn.merlin.layout.topbar.isMenuBarPickUp
import cn.merlin.network.Sender
import cn.merlin.test.SenderTest
import java.io.File

val sender = Sender()

val selectDevice = mutableStateOf(false)
val selectSettings = mutableStateOf(false)
val selectDetect = mutableStateOf(false)
val selectHistory = mutableStateOf(false)

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
                modifier = Modifier
                    .size(width,leftButtomMenuHeight.value)
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.Start
            ){
                item{
                    ListItem("Icons/computer.png","我的电脑",width)
                }
                item{
                    ListItem("Icons/computer.png","我的电脑",width)
                }
                item{
                    ListItem("Icons/computer.png","我的电脑",width)
                }
                item{
                    ListItem("Icons/computer.png","我的电脑",width)
                }
                item{
                    ListItem("Icons/computer.png","我的电脑",width)
                }
                item{
                    ListItem("Icons/computer.png","我的电脑",width)
                }
                item{
                    ListItem("Icons/computer.png","我的电脑",width)
                }
            }
            Spacer(modifier = Modifier.background(MaterialTheme.colorScheme.tertiary).height(2.dp).width(width))
            Column(
                modifier = Modifier.size(width = width, height = 700.dp - leftButtomMenuHeight.value),
                horizontalAlignment = Alignment.CenterHorizontally){
                MenuItem("Icons/search.webp","搜索设备",width)
                MenuItem("Icons/history.webp","发送历史",width)
                MenuItem("Icons/settings.webp","系统设置",width)
            }

        }
    }
}

@Composable
fun MenuItem(
    Icon: String,
    text: String,
    width: Dp,
){
    val imageWidth = animateDpAsState(if(isMenuBarPickUp.value) 20.dp else 30.dp,TweenSpec(TWEEN_DURATION))
    val textWidth = animateDpAsState(if(isMenuBarPickUp.value) 0.dp else 60.dp,TweenSpec(durationMillis = 200, delay = 200))

    FilledIconButton(
        modifier = Modifier.padding(0.dp).fillMaxWidth().height(52.dp),
        shape = MaterialTheme.shapes.extraSmall,
        onClick = {
                if (text == "搜索设备") {
                    sender.detectDevices()
                }
            }
    ){
        Row {
            Image(painter = painterResource(Icon), contentDescription = text, modifier = Modifier.size(imageWidth.value))
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
fun ListItem(
    Icon: String,
    text: String,
    width: Dp,
){
    val imageWidth = animateDpAsState(if(isMenuBarPickUp.value) 20.dp else 30.dp,TweenSpec(TWEEN_DURATION))
    val textWidth = animateDpAsState(if(isMenuBarPickUp.value) 0.dp else 60.dp,TweenSpec(durationMillis = 200, delay = 200))

    FilledIconButton(
        modifier = Modifier.padding(0.dp).fillMaxWidth().height(52.dp),
        shape = MaterialTheme.shapes.extraSmall,
        onClick = {
            val sender = Sender()
            sender.sendFileToSelectedDevice(DeviceModel(Device(deviceIpAddress = "127.0.0.1")),File("C:\\Users\\merlin\\Documents\\SimpleSender\\src\\main\\resources\\files\\file.zip"))
        }
    ){
        Row {
            Image(painter = painterResource(Icon), contentDescription = text, modifier = Modifier.size(imageWidth.value))
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