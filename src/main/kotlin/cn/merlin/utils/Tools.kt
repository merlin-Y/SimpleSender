package cn.merlin.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import cn.merlin.bean.model.DeviceModel
import cn.merlin.database.SenderDB
import cn.merlin.layout.theme.isInDarkMode
import cn.merlin.layout.theme.isSystemInDarkTheme
import kotlinx.coroutines.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Paths
import java.util.Properties

const val TIME_BETWEEN = 500
val resourcesPath = mutableStateOf("")
val settingsPath = mutableStateOf("")
val databasePath = mutableStateOf("")
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

fun createAllResourcesFiles() {
    val path = Paths.get("").toAbsolutePath().toString()
    resourcesPath.value = Paths.get(path, "/src/main/resources").toString()
    val file = File(resourcesPath.value)
    if (!file.exists()) {
        file.mkdirs()
        File(file.path + "/database/").mkdirs()
        File(file.path + "/settings/").mkdirs()
        File(file.path + "/database/sender.db").createNewFile()
        File(file.path + "/settings/settings.properties").createNewFile()
    }
    databasePath.value = file.path + "/database/sender.db"
    settingsPath.value = file.path + "/settings/settings.properties"
}

fun getAllSettings() {
    data.load(FileInputStream(settingsPath.value))
    if (!data.containsKey("IsInitialed")) initializeProperties(data)
    if (data["changeTheme"] == "0") changeTheme.value = 0
    else if (data["changeTheme"] == "1") changeTheme.value = 1
    else changeTheme.value = 2
}

fun getLocalDevice(localDeviceList: SnapshotStateList<DeviceModel>, senderDB: SenderDB) {
    if (localDeviceList.isNotEmpty()) return
    CoroutineScope(Dispatchers.IO).launch {
        val deviceList = senderDB.selectAllDevice()
        deviceList.forEach {
            localDeviceList.add(DeviceModel(it))
        }
    }
}

fun updateSettings(key: String, value: Int) {
    data[key] = value.toString()
    data.store(FileOutputStream(settingsPath.value), "$key Changed")
}

fun initializeProperties(data: Properties) {
    data["IsInitialed"] = "1"
    data["changeTheme"] = "0"
//    data["userProfile"] = getUserProfile()
    data.store(FileOutputStream(settingsPath.value), "Data Initialed")
}

@Composable
fun detectDarkMode() {
    CoroutineScope(Dispatchers.IO).launch {
        while (isActive) {
            delay(TIME_BETWEEN.toLong())
            isInDarkMode.value = isSystemInDarkTheme()
        }
    }
}