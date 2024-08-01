package cn.merlin.tools

import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cn.merlin.bean.Device
import cn.merlin.bean.model.DeviceViewModel
import cn.merlin.database.SenderDB
import cn.merlin.tools.DeviceConfiguration.getSavedList
import cn.merlin.tools.DeviceConfiguration.savedDeviceIdentifierSet
import cn.merlin.ui.theme.isInDarkMode
import cn.merlin.ui.theme.isSystemInDarkTheme
import kotlinx.coroutines.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.NetworkInterface
import java.nio.file.Paths
import java.util.Properties
import java.util.UUID

const val TIME_BETWEEN = 500
val currentDevice = mutableStateOf(DeviceViewModel(Device(deviceName = "我的设备")))
val resourcesPath = mutableStateOf("")
val settingsPath = mutableStateOf("")
val databasePath = mutableStateOf("")
val localDeviceName = mutableStateOf("")
val changeTheme = mutableStateOf(0)
val isWifiConnected = mutableStateOf(false)
val data = Properties()
val isDeviceFlushed = mutableStateOf(false)
val isSavedFlushed = mutableStateOf(false)

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
    currentDevice.value.deviceIdentifier.value = data["localDeviceIdentifier"].toString()
    currentDevice.value.deviceNickName.value = data["localDeviceName"].toString()
}

fun getDeviceName(){
    currentDevice.value.deviceName.value = System.getenv("COMPUTERNAME") ?: System.getenv("HOSTNAME")
}

fun getWifiAddress(): Boolean {
    try {
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()
        while (networkInterfaces.hasMoreElements()) {
            val networkInterface = networkInterfaces.nextElement()
            if(networkInterface.name.startsWith("wlan", ignoreCase = true) || networkInterface.name.startsWith("WI-FI", ignoreCase = true)) {
                val inetAddresses = networkInterface.inetAddresses
                while (inetAddresses.hasMoreElements()) {
                    val inetAddress = inetAddresses.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress.hostAddress.contains("192.168.")) {
                        isWifiConnected.value = inetAddress.hostAddress != ""
                        currentDevice.value.deviceIpAddress.value = inetAddress.hostAddress
                        return true
                    }
                }
            }
        }
        return false
    } catch (_: Exception) {
        return false
    }
}

fun getSavedDevice(senderDB: SenderDB) {
    if (getSavedList().isNotEmpty()) return
    CoroutineScope(Dispatchers.IO).launch {
        val deviceList = senderDB.selectAllDevice()
        deviceList.forEach {
            getSavedList().add(DeviceViewModel(it))
            savedDeviceIdentifierSet.add(it.deviceIdentifier)
        }
        isSavedFlushed.value = !isSavedFlushed.value
    }
}

fun updateSettings(key: String, value: String) {
    data[key] = value
    data.store(FileOutputStream(settingsPath.value), "$key Changed")
}

fun initializeProperties(data: Properties) {
    data["IsInitialed"] = "1"
    data["changeTheme"] = "0"
    data["localDeviceIdentifier"] = UUID.randomUUID().toString()
    data["localDeviceName"] = "-1"
    data["userProfile"] = getUserProfile()
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

fun getDeviceWidth(device: DeviceViewModel): Dp {
    return when(device.deviceType.value){
        "desktop" -> 260.dp
        "laptop" -> 190.dp
        "phone" -> 120.dp
        else -> 120.dp
    }
}