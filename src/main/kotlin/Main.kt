import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cn.merlin.layout.component.TittleBar
import cn.merlin.layout.theme.AppColorsProvider
import cn.merlin.layout.theme.MainTheme
import cn.merlin.layout.theme.isUserDarkThemeOn
import cn.merlin.utils.Settings
import cn.merlin.utils.checkIfContain
import org.jetbrains.exposed.sql.Column
import java.util.prefs.Preferences
import javax.naming.Context


fun main() = application {
    val windowstate = rememberWindowState()
    val offsetX = mutableStateOf(0f)
    val offsetY = mutableStateOf(0f)
    val data = Preferences.userRoot()

    if(!checkIfContain(data, "useDarkTheme")) data.putBoolean("useDarkTheme", false)

    Settings.set("useDarkTheme", mutableStateOf(data.getBoolean("useDarkTheme", isSystemInDarkTheme() || data.getBoolean("useDarkTheme", false))))

    MainTheme(Settings) {
        Window(
            onCloseRequest = ::exitApplication,
            title = "SimpleSender",
            icon = painterResource("Icons/PaperPlane.png"),
            state = windowstate,
            undecorated = true
        ){
            Column(){
                TittleBar("Icons/PaperPlane.png","SimpleSender",offsetX,offsetY,windowstate)
                Button(onClick = {
                    Settings.set("useDarkTheme" , mutableStateOf(!Settings.getValue("useDarkTheme").value))
                    data.putBoolean("useDarkTheme", Settings.getValue("useDarkTheme").value)
                    isUserDarkThemeOn.value = !isUserDarkThemeOn.value
                }){
                    println(AppColorsProvider.current.topBar.value)
                    Text(if(Settings.getValue("useDarkTheme").value) "turn on" else "turn off")
                }
            }

        }
    }
}
