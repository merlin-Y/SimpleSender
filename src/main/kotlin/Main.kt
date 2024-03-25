import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import cn.merlin.database.SenderDB
import cn.merlin.layout.leftMenu.leftMenuBar
import cn.merlin.layout.topbar.TittleBar
import cn.merlin.layout.theme.MainTheme
import cn.merlin.layout.topbar.isMenuBarPickUp
import cn.merlin.network.SenderServer
import cn.merlin.utils.getAllSettings
import cn.merlin.utils.getUserProfile
import moe.tlaster.precompose.PreComposeWindow

fun main() = application {
    getUserProfile()
    val windowState = rememberWindowState(size = DpSize(height = 700.dp, width = 900.dp))
    val offsetX = mutableStateOf(0f)
    val offsetY = mutableStateOf(0f)
    val senderDB = SenderDB()
    senderDB.createTables()
    val senderServer = SenderServer()
    val menuBarWidth = animateDpAsState(if (isMenuBarPickUp.value) 60.dp else 180.dp, TweenSpec(400))
    getAllSettings()

    MainTheme() {
        PreComposeWindow(
            onCloseRequest = ::exitApplication,
            title = "SimpleSender",
            icon = painterResource("Icons/PaperPlane.png"),
            state = windowState,
            undecorated = true,
            resizable = false
        ) {
            senderServer.startServer()
            App(menuBarWidth.value, offsetX, offsetY, windowState)
        }
    }
}

@Composable
fun App(menuBarWidth: Dp, offsetX: MutableState<Float>, offsetY: MutableState<Float>, windowState: WindowState) {
    Surface(
        modifier = Modifier.background(MaterialTheme.colorScheme.primary)
    ) {
        Column() {
            TittleBar("Icons/PaperPlane.png", "SimpleSender", offsetX, offsetY, windowState)
            Row {
                leftMenuBar(menuBarWidth)/*  MenuBarWidth */
                Surface {
                    setting(900.dp - menuBarWidth)
//                    DetectDevice(900.dp - menuBarWidth)/* 900.dp - MenuBarWidth */
                }
            }
        }
    }
}