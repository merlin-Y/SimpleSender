package cn.merlin.network

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import cn.merlin.bean.Device
import cn.merlin.bean.model.DeviceModel
import kotlinx.coroutines.*
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.Socket

class Scanner() {
    val DetectedDevices: SnapshotStateList<DeviceModel> = mutableStateListOf()
    val currentIpAddress = mutableStateOf("")

    suspend fun detectAddress() {
        val ipAddresses = mutableStateListOf<String>()

        getCurrentIpAddress()

        coroutineScope {
            val ipAddressIndex = currentIpAddress.value.substring(0, currentIpAddress.value.lastIndexOf("."))
            println("Scanning from $ipAddressIndex.1~$ipAddressIndex.255")
            for (i in 1..255) {
                launch {
                    val host = "$ipAddressIndex.$i"
                    if(host == currentIpAddress.value)  cancel()
                    try {
                        val socket = Socket()
                        withContext(Dispatchers.IO) {
                            socket.connect(InetSocketAddress(host, 19999), 100)
                        }
                        ipAddresses.add("$ipAddressIndex.$i")
                        socket.close()
                    } catch (e: Exception) {
//                            print(e)
                    }
                }
            }
        }
        println("Scanning accomplished")
    }

    private suspend fun getCurrentIpAddress() {
        coroutineScope {
            launch {
                try {
                    val networkInterface = NetworkInterface.getNetworkInterfaces()
                    while (networkInterface.hasMoreElements()) {
                        val networkInterface = networkInterface.nextElement()
                        val inetAddresses = networkInterface.inetAddresses
                        while (inetAddresses.hasMoreElements()) {
                            val inetAddress = inetAddresses.nextElement()
                            if (!inetAddress.isLoopbackAddress && inetAddress.hostAddress.contains("192.168.")) {
                                currentIpAddress.value = inetAddress.hostAddress
                            }
                        }
                    }
                } catch (_: Exception) {

                }
            }
        }
    }
}