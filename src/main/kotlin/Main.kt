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
import cn.merlin.bean.model.DeviceViewModel
import cn.merlin.bean.model.MessageVIewModel
import cn.merlin.database.SenderDB
import cn.merlin.network.NetworkController
import cn.merlin.ui.leftMenuBar
import cn.merlin.ui.pages.detect
import cn.merlin.ui.pages.history
import cn.merlin.ui.pages.message
import cn.merlin.ui.pages.settings
import cn.merlin.ui.TittleBar
import cn.merlin.ui.theme.MainTheme
import cn.merlin.ui.isMenuBarPickUp
import cn.merlin.network.Receiver
import cn.merlin.network.Sender
import cn.merlin.tools.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
fun App( menuBarWidth: Dp, windowState: WindowState) {
    createAllResourcesFiles()
    getAllSettings()
    getUserProfile()
    detectDarkMode()

    PreComposeApp {
        val savedDeviceList: SnapshotStateList<DeviceViewModel> = remember{ mutableStateListOf() }
        val detectedDeviceList: SnapshotStateList<DeviceViewModel> = remember{ mutableStateListOf() }
        val navigator = rememberNavigator()
        val messageList: SnapshotStateList<MessageVIewModel> = mutableStateListOf()
        val senderDB = SenderDB()
        val networkController = NetworkController()

        LaunchedEffect(Unit){
            withContext(Dispatchers.IO){
                senderDB.createTables()
                getSavedDevice(savedDeviceList,senderDB)
            }
            withContext(Dispatchers.Default){
                getDeviceName()
                networkController.startNetworkControl(detectedDeviceList)
            }
        }

        Surface(
            modifier = Modifier.background(MaterialTheme.colorScheme.primary)
        ) {
            Column {
                TittleBar("Icons/PaperPlane.png", "SimpleSender", windowState)
                Row {
                    leftMenuBar(menuBarWidth, navigator,savedDeviceList)/*  MenuBarWidth */
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
                                detect(900.dp - menuBarWidth, 700.dp,savedDeviceList,detectedDeviceList)
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
