import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cn.merlin.layout.theme.MainTheme


fun main() = application {
    val windowstate = rememberWindowState()
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    MainTheme {
        Window(
            onCloseRequest = ::exitApplication,
//            title = "SimpleSender",
            icon = painterResource("Icons/PaperPlane.png"),
            state = windowstate,
            undecorated = true
        ){
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(47.5.dp)
                    .background(MaterialTheme.colors.primary)
                    .pointerInput(Unit){
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                        windowstate.position = WindowPosition(windowstate.position.x +offsetX.toDp(),windowstate.position.y + offsetY.toDp())
                    } }
            ) {
                Column {
                    IconButton(
                        modifier = Modifier
                            .padding(start = 10.dp, top = 10.dp)
                            .height(22.5.dp)
                            .width(25.dp),
                        onClick = {

                        }
                    ){
                        Image(painterResource("Icons/PaperPlane.png"),"", modifier = Modifier.height(22.5.dp).width(25.dp).padding(0.dp))
                    }
                }
            }
        }
    }
}
