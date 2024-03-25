package cn.merlin.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

val Settings: SnapshotStateMap<String, MutableState<Int>> = mutableStateMapOf()
val followSystemTheme = mutableStateOf(true)
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
    println(userProfile)
    return userProfile
}

fun getAllSettings(){
    data.load(FileInputStream("src/main/resources/settings/settings.properties"))
    if(!data.containsKey("IsInitialed")) initializeProperties(data)
    if(data["userTheme"] == "0")    followSystemTheme.value = true
    else {
        followSystemTheme.value = false
        Settings["userTheme"]?.value = when{
            data["userTheme"] == "0" -> 0
            data["userTheme"] == "1" -> 1
            else -> 0
        }
    }
}

fun updateSettings(key: String,value: Int){

}

fun initializeProperties(data: Properties){
    data["IsInitialed"] = 1
    data["userTheme"] = 0
//    data["userProfile"] = getUserProfile()
    data.store(FileOutputStream("src/main/resources/settings/settings.properties"),"Data Initialed")
}