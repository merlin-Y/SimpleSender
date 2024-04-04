import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import cn.merlin.bean.model.DeviceModel
import cn.merlin.database.SenderDB
import cn.merlin.layout.leftMenu.leftMenuBar
import cn.merlin.layout.mainWindow.detect
import cn.merlin.layout.mainWindow.history
import cn.merlin.layout.mainWindow.message
import cn.merlin.layout.mainWindow.settings
import cn.merlin.layout.topbar.TittleBar
import cn.merlin.layout.theme.MainTheme
import cn.merlin.layout.topbar.isMenuBarPickUp
import cn.merlin.network.Receiver
import cn.merlin.utils.*
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

fun main() = application {

    val windowState = rememberWindowState(size = DpSize(height = 700.dp, width = 900.dp))
    val menuBarWidth = animateDpAsState(if (isMenuBarPickUp.value) 60.dp else 180.dp, TweenSpec(400))
    val localDeviceList = remember { mutableStateListOf<DeviceModel>() }
    @Composable
    fun App(localDeviceList: SnapshotStateList<DeviceModel>, menuBarWidth: Dp, windowState: WindowState,senderDB: SenderDB) {
        PreComposeApp {
            val navigator = rememberNavigator()
            Surface(
                modifier = Modifier.background(MaterialTheme.colorScheme.primary)
            ) {
                Column {
                    TittleBar("Icons/PaperPlane.png", "SimpleSender", windowState)
                    Row {
                        leftMenuBar(localDeviceList, menuBarWidth, navigator)/*  MenuBarWidth */
                        Surface {
                            message(900.dp - menuBarWidth, 700.dp,senderDB)
                            NavHost(
                                navigator = navigator,
                                initialRoute = "/detect",
                                navTransition = NavTransition()
                            ) {
                                scene(
                                    route = "/message",
                                    navTransition = NavTransition()
                                ) {
                                    message(900.dp - menuBarWidth, 700.dp,senderDB)
                                }
                                scene(
                                    route = "/detect",
                                    navTransition = NavTransition()
                                ) {
                                    detect(localDeviceList, 900.dp - menuBarWidth, 700.dp)
                                }
                                scene(
                                    route = "/settings",
                                    navTransition = NavTransition()
                                ) {
                                    settings(900.dp - menuBarWidth, 700.dp)
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
    }

    MainTheme {
        Window(
            onCloseRequest = ::exitApplication,
            title = "SimpleSender",
            icon = painterResource("Icons/PaperPlane.png"),
            state = windowState,
            undecorated = true,
            resizable = false
        ) {
            createAllResourcesFiles()
            val server = Receiver()
            server.startServer()
            val senderDB = SenderDB()
            senderDB.createTables()
            getAllSettings()
            getUserProfile()
            detectDarkMode()
            getLocalDevice(localDeviceList, senderDB)

            App(localDeviceList, menuBarWidth.value, windowState,senderDB)
        }
    }
}
