package cn.merlin.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cn.merlin.layout.theme.isSystemInDarkTheme
import kotlinx.coroutines.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

const val TIME_BETWEEN = 500
val changeTheme = mutableStateOf(0)
val data = Properties()

fun getUserProfile(): String {
    var userProfile = ""
    try {
        val osName = System.getProperty("os.name")
        userProfile = when {
            osName.contains("Win") -> System.getenv("USERPROFILE") + "\\Downloads"
            osName.contains("Mac") || osName.contains("nix") || osName.contains("nux") -> System.getProperty("user.home") + "\\Downloads"
            else -> "UNKNOWN"
        }
    } catch (_: Exception) {
    }
    return "$userProfile\\SimpleSender\\"
}

fun getAllSettings(){
    data.load(FileInputStream("src/main/resources/settings/settings.properties"))
    if(!data.containsKey("IsInitialed")) initializeProperties(data)
    if(data["changeTheme"] == "0")    changeTheme.value = 0
    else if(data["changeTheme"] == "1")   changeTheme.value = 1
    else changeTheme.value = 2
}

fun updateSettings(key: String,value: Int){
    data[key] = value.toString()
    data.store(FileOutputStream("src/main/resources/settings/settings.properties"),"$key Changed")
}

fun initializeProperties(data: Properties){
    data["IsInitialed"] = "1"
    data["changeTheme"] = "0"
//    data["userProfile"] = getUserProfile()
    data.store(FileOutputStream("src/main/resources/settings/settings.properties"),"Data Initialed")
}

@Composable
fun detectDarkMode(isInDarkMode: MutableState<Boolean>){
    CoroutineScope(Dispatchers.IO).launch {
        while (isActive){
            delay(TIME_BETWEEN.toLong())
            isInDarkMode.value = isSystemInDarkTheme()
        }
    }
}