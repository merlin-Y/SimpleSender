import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.layout.mainWindow.currentDevice
import cn.merlin.layout.mainWindow.detect
import cn.merlin.layout.mainWindow.message
import cn.merlin.layout.topbar.isMenuBarPickUp

@Composable
fun mainWindow(
    width: Dp
) {
    val messageWidth = animateDpAsState(0.dp)
    val messageHeight = mutableStateOf(0.dp)
    val settingsWidth = mutableStateOf(0.dp)
    val settingsHeight = animateDpAsState(0.dp)
    val detectWidth = mutableStateOf(0.dp)
    val detectHeight = mutableStateOf(0.dp)
    val historyWidth = mutableStateOf(0.dp)
    val historyHeight = mutableStateOf(0.dp)

    val appIconStartDp = animateDpAsState(if (isMenuBarPickUp.value) 20.dp else 0.dp)

    Surface(
        modifier = Modifier.size(width, 700.dp),
        color = MaterialTheme.colorScheme.secondary
    ) {
//        Column() {
//            Row() {
//                message(messageWidth.value, messageHeight.value)
//                detect(detectWidth.value, detectHeight.value)
//            }
            setting(settingsWidth.value, settingsHeight.value)
//        }
    }
}