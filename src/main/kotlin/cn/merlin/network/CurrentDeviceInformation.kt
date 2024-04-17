package cn.merlin.network

import cn.merlin.bean.Device
import java.net.NetworkInterface

object CurrentDeviceInformation {
    private var currentDevice: Device? = null

    private fun getDeviceInformation() {
        currentDevice = Device()
        val deviceName = System.getenv("COMPUTERNAME") ?: System.getenv("HOSTNAME")
        currentDevice!!.deviceName = deviceName
        try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                if(networkInterface.name.startsWith("wlan", ignoreCase = true) || networkInterface.name.startsWith("WI-FI", ignoreCase = true)) {
                    val inetAddresses = networkInterface.inetAddresses
                    while (inetAddresses.hasMoreElements()) {
                        val inetAddress = inetAddresses.nextElement()
                        if (!inetAddress.isLoopbackAddress && inetAddress.hostAddress.contains("192.168.")) {
                            currentDevice!!.deviceIpAddress = inetAddress.hostAddress
                        }
                    }
                }
            }
        } catch (_: Exception) { }
    }

    fun getInformation(): Device {
        currentDevice?.let {
            return currentDevice!!
        }
        getDeviceInformation()
        return currentDevice!!
    }
}