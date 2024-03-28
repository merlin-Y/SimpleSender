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
import cn.merlin.layout.mainWindow.detect
import cn.merlin.layout.mainWindow.history
import cn.merlin.layout.mainWindow.message
import cn.merlin.layout.mainWindow.settings
import cn.merlin.layout.topbar.TittleBar
import cn.merlin.layout.theme.MainTheme
import cn.merlin.layout.topbar.isMenuBarPickUp
import cn.merlin.network.Server
import cn.merlin.utils.detectDarkMode
import cn.merlin.utils.getAllSettings
import cn.merlin.utils.getUserProfile
import moe.tlaster.precompose.PreComposeWindow
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

fun main() = application {

    val windowState = rememberWindowState(size = DpSize(height = 700.dp, width = 900.dp))
    val senderDB = SenderDB()
    senderDB.createTables()
    val server = Server()
    val menuBarWidth = animateDpAsState(if (isMenuBarPickUp.value) 60.dp else 180.dp, TweenSpec(400))
    val localDevices: MutableList<Device> = mutableStateListOf()
    val isInDarkMode = mutableStateOf(true)
    getAllSettings()
    getUserProfile()
    detectDarkMode(isInDarkMode)

    val devices = senderDB.selectAllDevice()

    MainTheme(isInDarkMode) {
        PreComposeWindow(
            onCloseRequest = ::exitApplication,
            title = "SimpleSender",
            icon = painterResource("Icons/PaperPlane.png"),
            state = windowState,
            undecorated = true,
            resizable = false
        ) {
            server.startServer()
            App(menuBarWidth.value, windowState)
        }
    }
}

@Composable
fun App(menuBarWidth: Dp, windowState: WindowState) {
    val navigator = rememberNavigator()
    Surface(
        modifier = Modifier.background(MaterialTheme.colorScheme.primary)
    ) {
        Column() {
            TittleBar("Icons/PaperPlane.png", "SimpleSender", windowState)
            Row {
                leftMenuBar(menuBarWidth, navigator)/*  MenuBarWidth */
                Surface {
                    message(900.dp - menuBarWidth, 700.dp)
                    NavHost(
                        navigator = navigator,
                        initialRoute = "/message",
                        navTransition = NavTransition()
                    ) {
                        scene(
                            route = "/message",
                            navTransition = NavTransition()
                        ) {
                            message(900.dp - menuBarWidth, 700.dp)
                        }
                        scene(
                            route = "/detect",
                            navTransition = NavTransition()
                        ) {
                            detect(900.dp - menuBarWidth, 700.dp)
                        }
                        scene(
                            route = "/settings",
                            navTransition = NavTransition()
                        ) {
                            settings(900.dp - menuBarWidth, 700.dp, navigator)
                        }
                        scene(
                            route = "/history",
                            navTransition = NavTransition()
                        ) {
                            history(900.dp - menuBarWidth, 700.dp)
                        }
                    }
                }
            }
        }
    }
}