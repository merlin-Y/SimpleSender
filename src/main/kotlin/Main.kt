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
import cn.merlin.bean.model.MessageModel
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
import cn.merlin.network.Sender
import cn.merlin.utils.*
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
fun App( menuBarWidth: Dp, windowState: WindowState) {
    PreComposeApp {
        val localDeviceList: SnapshotStateList<DeviceModel> = remember{ mutableStateListOf() }
        val detectedDeviceList: SnapshotStateList<DeviceModel> = remember{ mutableStateListOf() }
        val navigator = rememberNavigator()
        val messageList: SnapshotStateList<MessageModel> = mutableStateListOf()

        val sender = Sender()
        val receiver = Receiver()

        createAllResourcesFiles()
        val senderDB = SenderDB()
        senderDB.createTables()
        getAllSettings()
        getUserProfile()
        detectDarkMode()

        LaunchedEffect(Unit){
            getLocalDevice(localDeviceList,senderDB)
            sender.startScanning()
            receiver.startServer(detectedDeviceList)
        }

        Surface(
            modifier = Modifier.background(MaterialTheme.colorScheme.primary)
        ) {
            Column {
                TittleBar("Icons/PaperPlane.png", "SimpleSender", windowState)
                Row {
                    leftMenuBar(menuBarWidth, navigator,localDeviceList)/*  MenuBarWidth */
                    Surface {
//                            message(900.dp - menuBarWidth, 700.dp,senderDB,messageList)
                        NavHost(
                            navigator = navigator,
                            initialRoute = "/detect",
                            navTransition = NavTransition()
                        ) {
                            scene(
                                route = "/message",
                                navTransition = NavTransition()
                            ) {
                                message(900.dp - menuBarWidth, 700.dp,senderDB,messageList)
                            }
                            scene(
                                route = "/detect",
                                navTransition = NavTransition()
                            ) {
                                detect(900.dp - menuBarWidth, 700.dp,detectedDeviceList,localDeviceList)
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

fun main() = application {

    val windowState = rememberWindowState(size = DpSize(height = 700.dp, width = 900.dp))
    val menuBarWidth = animateDpAsState(if (isMenuBarPickUp.value) 60.dp else 180.dp, TweenSpec(400))
    MainTheme {
        Window(
            onCloseRequest = ::exitApplication,
            title = "SimpleSender",
            icon = painterResource("Icons/PaperPlane.png"),
            state = windowState,
            undecorated = true,
            resizable = false
        ) {
            App(menuBarWidth.value, windowState)
        }
    }
}
