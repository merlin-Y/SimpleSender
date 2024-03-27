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
import cn.merlin.bean.Device
import cn.merlin.database.SenderDB
import cn.merlin.layout.leftMenu.leftMenuBar
import cn.merlin.layout.topbar.TittleBar
import cn.merlin.layout.theme.MainTheme
import cn.merlin.layout.topbar.isMenuBarPickUp
import cn.merlin.network.Server
import cn.merlin.utils.getAllSettings
import cn.merlin.utils.getUserProfile
import moe.tlaster.precompose.PreComposeWindow

fun main() = application {
    val windowState = rememberWindowState(size = DpSize(height = 700.dp, width = 900.dp))
    val offsetX = mutableStateOf(0f)
    val offsetY = mutableStateOf(0f)
    val senderDB = SenderDB()
    senderDB.createTables()
    val server = Server()
    val menuBarWidth = animateDpAsState(if (isMenuBarPickUp.value) 60.dp else 180.dp, TweenSpec(400))
    val localDevices: MutableList<Device> = mutableStateListOf()
    getAllSettings()
    getUserProfile()

    val devices = senderDB.selectAllDevice()

    MainTheme() {
        PreComposeWindow(
            onCloseRequest = ::exitApplication,
            title = "SimpleSender",
            icon = painterResource("Icons/PaperPlane.png"),
            state = windowState,
            undecorated = true,
            resizable = false
        ) {
            server.startServer()
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
//                    mainWindow(900.dp - menuBarWidth)
                    setting(900.dp - menuBarWidth,700.dp)
                }
            }
        }
    }
}