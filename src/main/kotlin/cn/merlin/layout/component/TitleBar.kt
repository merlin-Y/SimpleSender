package cn.merlin.layout.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.Surface
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import cn.merlin.layout.theme.AppColorsProvider

@Composable
fun TittleBar(
    icon: String,
    title: String,
    offsetX: MutableState<Float>,
    offsetY: MutableState<Float>,
    windowstate: WindowState
){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .background(AppColorsProvider.current.topBar)
            .pointerInput(Unit){
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX.value += dragAmount.x
                    offsetY.value += dragAmount.y
                    windowstate.position = WindowPosition(windowstate.position.x + offsetX.value.toDp(),windowstate.position.y + offsetY.value.toDp())
                } }
    ) {
        Row {
            IconButton(
                modifier = Modifier
                    .padding(top = 7.5.dp, end = 10.dp)
                    .height(25.dp)
                    .width(25.dp),
                onClick = {

                }
            ){
                Icon(Icons.Filled.Menu,"", tint = AppColorsProvider.current.onPrimaryStable, modifier = Modifier.height(20.dp).width(20.dp).padding(0.dp))
            }
            IconButton(
                modifier = Modifier
                    .padding(top = 7.5.dp, end = 10.dp)
                    .height(25.dp)
                    .width(25.dp),
                onClick = {

                }
            ){
                Image(painterResource(icon),"", modifier = Modifier.height(20.dp).width(20.dp).padding(0.dp))
            }
            Text(
                title,
                modifier = Modifier.width(windowstate.size.width - 100.dp).fillMaxHeight(),
                color = AppColorsProvider.current.onPrimary,
                fontSize = 16.sp,
                lineHeight = 32.sp
            )
        }
    }
}