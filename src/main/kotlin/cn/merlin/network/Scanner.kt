package cn.merlin.network

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import cn.merlin.bean.Device
import cn.merlin.bean.model.DeviceModel
import kotlinx.coroutines.*
import java.io.ObjectInputStream
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.Socket

class Scanner() {
    val DetectedDevices: SnapshotStateList<DeviceModel> = mutableStateListOf()
//    val DetectedAddresses: SnapshotStateList<String> = mutableStateListOf()
    var currentDevice = Device()

    suspend fun detectDevices() {
        currentDevice = CurrentDeviceInformation.getInformation()
        coroutineScope {
            val ipAddressIndex = currentDevice.deviceIpAddress.substring(0, currentDevice.deviceIpAddress.lastIndexOf("."))
            println("Scanning from $ipAddressIndex.1~$ipAddressIndex.255")
            for (i in 1..255) {
                launch {
                    val host = "$ipAddressIndex.$i"
                    if(host == currentDevice.deviceIpAddress)  cancel()
                    try {
                        val socket = Socket()
                        withContext(Dispatchers.IO) {
                            socket.connect(InetSocketAddress(host, 19999), 100)
                            val objectInputStream = ObjectInputStream(socket.getInputStream())
                            val device = objectInputStream.readObject() as Device
                            DetectedDevices.add(DeviceModel(device))
                            println(device)
                        }
//                        DetectedAddresses.add("$ipAddressIndex.$i")
                        socket.close()
                    } catch (e: Exception) {
//                            print(e)
                    }
                }
            }
        }
        println("${currentDevice.deviceName} ${currentDevice.deviceIpAddress} ${currentDevice.deviceMacAddress}")
        println("Scanning accomplished")
    }
}