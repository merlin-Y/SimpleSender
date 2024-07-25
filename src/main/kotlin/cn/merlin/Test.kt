package cn.merlin

import androidx.compose.runtime.TestOnly
import cn.merlin.bean.Device
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

fun main() {
    val test = Test()
    test.startDetectCodeReceiver()
}

class Test() {
    val currentDevice = Device(deviceName = "redmi k50", deviceIpAddress = "192.168.1.11")
    private val receiveCommandCodePort = 29999
    private val receiveDeviceCodePort = 30000
    private val receiveMessageCodePort = 30001

    fun startDetectCodeReceiver() {
        while (true) {
            val receiveSocket = DatagramSocket(receiveCommandCodePort)
            val receiveBuffer = ByteArray(64)
            val receivePacket = DatagramPacket(receiveBuffer, receiveBuffer.size)
            try {
                receiveSocket.receive(receivePacket)
                receiveSocket.close()
                val length = (receivePacket.data[0].toInt() shl 8) or (receivePacket.data[1].toInt() and 0xFF)
                val buffer = ByteArray(length)
                System.arraycopy(receivePacket.data, 2, buffer, 0, length)
                val cnb = buffer.toString(Charsets.UTF_8)
                if (cnb.contains("detectCode")) {
                    try {
                        val deviceJson = Json.encodeToString(currentDevice)
                        val ipAddress = cnb.substring(cnb.lastIndexOf(';') + 1, cnb.length)
                        if (ipAddress != currentDevice.deviceIpAddress) {
                            val sendMessageArray = ByteArray(deviceJson.length + 16)
                            sendMessageArray[0] = ((deviceJson.length + 14) shr 8).toByte()
                            sendMessageArray[1] = (deviceJson.length + 14).toByte()
                            System.arraycopy(
                                "receiveDevice;".toByteArray(Charsets.UTF_8) + deviceJson.toByteArray(),
                                0,
                                sendMessageArray,
                                2,
                                sendMessageArray.size
                            )
                            val socket = DatagramSocket()
                            val packet = DatagramPacket(
                                sendMessageArray,
                                deviceJson.length + 12,
                                InetAddress.getByName(ipAddress),
                                receiveDeviceCodePort
                            )
                            socket.send(packet)
                            socket.close()
                        }
                    } catch (_: Exception) {
                    }
                }
            } catch (_: Exception) {
                receiveSocket.close()
            }
        }
    }
}