package cn.merlin.utils

import androidx.compose.runtime.mutableStateOf
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

val changeTheme = mutableStateOf(0)
val data = Properties()

fun getUserProfile(): String {
    var userProfile = ""
    try {
        val osName = System.getProperty("os.name")
        userProfile = when {
            osName.contains("Win") -> System.getenv("USERPROFILE") + "\\Documents\\"
            osName.contains("Mac") || osName.contains("nix") || osName.contains("nux") -> System.getProperty("user.home") + "\\Documents\\"
            else -> "UNKNOWN"
        }
    } catch (_: Exception) {
    }
    return userProfile
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