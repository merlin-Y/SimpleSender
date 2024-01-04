import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cn.merlin.layout.checkdevice.DetectDevice
import cn.merlin.layout.leftMenu.MainLeftMenu
import cn.merlin.layout.leftMenu.NavigatorManager
import cn.merlin.layout.leftMenu.leftMenuBar
import cn.merlin.layout.topbar.TittleBar
import cn.merlin.layout.theme.MainTheme
import cn.merlin.layout.theme.TWEEN_DURATION
import cn.merlin.layout.topbar.isMenuBarPickUp
import cn.merlin.utils.Settings
import cn.merlin.utils.checkIfContain
import moe.tlaster.precompose.PreComposeWindow
import moe.tlaster.precompose.navigation.rememberNavigator
import java.util.prefs.Preferences


fun main() = application {
    initImageLoader()
    val windowstate = rememberWindowState(size = DpSize(height = 700.dp, width = 900.dp))
    val offsetX = mutableStateOf(0f)
    val offsetY = mutableStateOf(0f)
    val data = Preferences.userRoot()
    val MenuBarWidth = animateDpAsState(if(isMenuBarPickUp.value) 60.dp else 180.dp,TweenSpec(400))

//    NavigatorManager.navigator = rememberNavigator()

    if(!checkIfContain(data, "useDarkTheme")) data.putBoolean("useDarkTheme", isSystemInDarkTheme())

    Settings.set("useDarkTheme", mutableStateOf(data.getBoolean("useDarkTheme", false)))


    MainTheme(Settings) {
        Window(
            onCloseRequest = ::exitApplication,
            title = "SimpleSender",
            icon = painterResource("Icons/PaperPlane.png"),
            state = windowstate,
            undecorated = true,
            resizable = false
        ){
            App(MenuBarWidth.value,offsetX,offsetY,windowstate)
        }
    }
}

private fun initImageLoader() {
//    ImageLoader.configuration(rootDirectory = File(System.getProperty("user.home") + File.separator + "Library" + File.separator + "NCMusicDesktop"))
//    println(System.getProperty("user.home") + File.separator + "Library" + File.separator + "NCMusicDesktop")
}

@Composable
fun App(MenuBarWidth: Dp,offsetX: MutableState<Float>,offsetY: MutableState<Float>,windowstate: WindowState){
    Surface(
        modifier = Modifier.background(MaterialTheme.colorScheme.primary)
    ) {
        Column(){
            TittleBar("Icons/PaperPlane.png","SimpleSender",offsetX,offsetY,windowstate)
            Row {
                leftMenuBar(MenuBarWidth)/*  MenuBarWidth */
                DetectDevice(900.dp - MenuBarWidth)/* 900.dp - MenuBarWidth */
            }
        }
    }
}