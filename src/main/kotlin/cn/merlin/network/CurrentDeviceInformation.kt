package cn.merlin.network

import cn.merlin.bean.Device
import java.net.NetworkInterface

object CurrentDeviceInformation {
    private val currentDevice = Device()

    private fun getDeviceInformation() {
        val deviceName = System.getenv("COMPUTERNAME") ?: System.getenv("HOSTNAME")
        currentDevice.deviceName = deviceName
        try {
            val networkInterface = NetworkInterface.getNetworkInterfaces()
            while (networkInterface.hasMoreElements()) {
                val networkInterface = networkInterface.nextElement()
                val inetAddresses = networkInterface.inetAddresses
                while (inetAddresses.hasMoreElements()) {
                    val inetAddress = inetAddresses.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress.hostAddress.contains("192.168.")) {
                        val macAddress = networkInterface.hardwareAddress
                        val macAddressStr = macAddress.joinToString(":") { "%02X".format(it) }
                        currentDevice.deviceMacAddress = macAddressStr
                        currentDevice.deviceIpAddress = inetAddress.hostAddress
                    }
                }
            }
        } catch (_: Exception) { }
    }

    fun getInformation(): Device {
        getDeviceInformation()
        return currentDevice
    }
}