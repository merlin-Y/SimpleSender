package cn.merlin.network

import cn.merlin.bean.Device
import cn.merlin.bean.Message
import cn.merlin.bean.Request
import cn.merlin.bean.model.DeviceViewModel
import cn.merlin.bean.model.MessageVIewModel
import cn.merlin.bean.model.RequestViewModel
import cn.merlin.tools.*
import cn.merlin.tools.DeviceConfiguration.detectedDeviceIdentifierSet
import cn.merlin.tools.DeviceConfiguration.getDetectedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import moe.tlaster.precompose.navigation.Navigator
import java.io.File
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class Receiver {
    private val receiveCommandCodePort = 19999

    fun startCommandCodeReceiver(currentDevice: Device, navigator: Navigator) {
        CoroutineScope(Dispatchers.IO).launch {
            val receiveSocket = DatagramSocket(receiveCommandCodePort)
            val receiveBuffer = ByteArray(1024)
            val receivePacket = DatagramPacket(receiveBuffer, receiveBuffer.size)
            try {
                while (true) {
                    receiveSocket.receive(receivePacket)
                    val length =
                        (receivePacket.data[0].toInt() shl 8) or (receivePacket.data[1].toInt() and 0xFF)
                    val buffer = ByteArray(length)
                    System.arraycopy(receivePacket.data, 2, buffer, 0, length)
                    val cnb = buffer.toString(Charsets.UTF_8)
                    if (cnb.contains("detectCode")) {
                        CoroutineScope(Dispatchers.IO).launch{
                            val socket = DatagramSocket()
                            try {
                                val deviceJson = Json.encodeToString(currentDevice)
                                val ipAddress = cnb.substring(cnb.lastIndexOf(';') + 1, cnb.length)
                                if (ipAddress != currentDevice.deviceIpAddress) {
                                    val sendMessage = "receiveDevice;$deviceJson"
                                    val sendMessageArray = ByteArray(sendMessage.length + 2)
                                    sendMessageArray[0] = ((sendMessage.length) shr 8).toByte()
                                    sendMessageArray[1] = (sendMessage.length).toByte()
                                    System.arraycopy(
                                        sendMessage.toByteArray(Charsets.UTF_8),
                                        0,
                                        sendMessageArray,
                                        2,
                                        sendMessage.length
                                    )
                                    val packet = DatagramPacket(
                                        sendMessageArray,
                                        sendMessageArray.size,
                                        InetAddress.getByName(ipAddress),
                                        receiveCommandCodePort
                                    )
                                    socket.send(packet)
                                    socket.close()
                                }
                            } catch (_: Exception) {
                                socket.close()
                            }
                        }
                    }else if(cnb.contains("keepCode")){
                        CoroutineScope(Dispatchers.IO).launch{
                            val ipAddress = cnb.substring(cnb.lastIndexOf(';') + 1, cnb.lastIndexOf(':'))
                            val port = cnb.substring(cnb.lastIndexOf(':') + 1, cnb.length).toInt()
                            val socket = DatagramSocket()
                            val sendMessage = "keepCode;${currentDevice.deviceIpAddress}"
                            val sendMessageArray = ByteArray(sendMessage.length + 2)
                            sendMessageArray[0] = ((sendMessage.length) shr 8).toByte()
                            sendMessageArray[1] = (sendMessage.length).toByte()
                            System.arraycopy(
                                sendMessage.toByteArray(Charsets.UTF_8),
                                0,
                                sendMessageArray,
                                2,
                                sendMessage.length
                            )
                            try {
                                val packet = DatagramPacket(
                                    sendMessageArray,
                                    sendMessageArray.size,
                                    InetAddress.getByName(ipAddress),
                                    port
                                )
                                socket.send(packet)
                            } catch (_: Exception) {
                                socket.close()
                            }
                            socket.close()
                        }
                    }else if(cnb.contains("receiveDevice")){
                        CoroutineScope(Dispatchers.IO).launch{
                            val device = Json.decodeFromString<Device>(
                                cnb.substring(
                                    cnb.lastIndexOf(';') + 1,
                                    cnb.length
                                )
                            )
                            if (!detectedDeviceIdentifierSet.contains(device.deviceIdentifier)) {
                                detectedDeviceIdentifierSet.add(device.deviceIdentifier)
                                getDetectedList().add(DeviceViewModel(device))
                            } else {
                                getDetectedList().forEach {
                                    if (it.deviceIdentifier.value == device.deviceIdentifier) it.isConnected.value =
                                        true
                                }
                            }
                            isDeviceFlushed.value = !isDeviceFlushed.value
                        }
                    }else if (cnb.contains("sendMessage")) {
                        CoroutineScope(Dispatchers.IO).launch{
                            val ipAddress = cnb.substring(cnb.lastIndexOf(';') + 1, cnb.lastIndexOf(':'))
                            val port = cnb.substring(cnb.lastIndexOf(':') + 1, cnb.length).toInt()
                            val request = RequestViewModel(Request(ipAddress,port))
                            receiveRequestList.add(request)
                            if(currentPage.value != "/message") {
                                currentPage.value = "message"
                                navigator.navigate("/message")
                            }
                            
//                            val freePort = getFreePort()
//                            val datagramSocket = DatagramSocket()
//                            val sendMessage = "sendMessage;accessRequest:${freePort}"
//                            val sendMessageArray = ByteArray(sendMessage.length + 2)
//                            sendMessageArray[0] = ((sendMessage.length) shr 8).toByte()
//                            sendMessageArray[1] = (sendMessage.length).toByte()
//                            System.arraycopy(
//                                sendMessage.toByteArray(Charsets.UTF_8),
//                                0,
//                                sendMessageArray,
//                                2,
//                                sendMessage.length
//                            )
//                            try {
//                                val packet = DatagramPacket(
//                                    sendMessageArray,
//                                    sendMessageArray.size,
//                                    InetAddress.getByName(ipAddress),
//                                    port
//                                )
//                                datagramSocket.send(packet)
//                                datagramSocket.close()
//                            }catch (_: Exception){
//                                datagramSocket.close()
//                            }
                        }
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

//    fun startDeviceCodeReceiver() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val receiverSocket = DatagramSocket(receiveDeviceCodePort)
//            val receiverBuffer = ByteArray(512)
//            val receiverPacket = DatagramPacket(receiverBuffer, receiverBuffer.size)
//            try {
//                while (true) {
//                    receiverSocket.receive(receiverPacket)
//                    val length =
//                        (receiverPacket.data[0].toInt() shl 8) or (receiverPacket.data[1].toInt() and 0xFF)
//                    val buffer = ByteArray(length)
//                    System.arraycopy(receiverPacket.data, 2, buffer, 0, length)
//                    val cnb = buffer.toString(Charsets.UTF_8)
//                    if (cnb.contains("receiveDevice")) {
//                        val device = Json.decodeFromString<Device>(
//                            cnb.substring(
//                                cnb.lastIndexOf(';') + 1,
//                                cnb.length
//                            )
//                        )
//                        if (!detectedDeviceIdentifierSet.contains(device.deviceIdentifier)) {
//                            detectedDeviceIdentifierSet.add(device.deviceIdentifier)
//                            getDetectedList().add(DeviceViewModel(device))
//                        }else{
//                            getDetectedList().forEach {
//                                if(it.deviceIdentifier.value == device.deviceIdentifier)    it.isConnected.value = true
//                            }
//                        }
//                        isDeviceFlushed.value = !isDeviceFlushed.value
//                    }
//                }
//            } catch (_: Exception) {
//            }
//        }
//    }

//    fun startMessageCodeReceiver() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val receiverSocket = DatagramSocket(receiveMessageCodePort)
//            val receiverBuffer = ByteArray(1024)
//            val receiverPacket = DatagramPacket(receiverBuffer, receiverBuffer.size)
//            try {
//                while (true) {
//                    receiverSocket.receive(receiverPacket)
//                    val length =
//                        (receiverPacket.data[0].toInt() shl 8) or (receiverPacket.data[1].toInt() and 0xFF)
//                    val buffer = ByteArray(length)
//                    System.arraycopy(receiverPacket.data, 2, buffer, 0, length)
//                    val cnb = buffer.toString(Charsets.UTF_8)
//                    if (cnb.contains("receiveFile")) {
//
//                        receiveFile()
//                    }
//                }
//            } catch (_: Exception) {
//            }
//        }
//    }

//    private fun receiveFile(receivedFile: cn.merlin.bean.File, port: Int) {
//        val packetSize = 10240
//        var packetNumber = 0
//        val socket = DatagramSocket(port)
//        try {
//            val receiveData = ByteArray(receivedFile.dataSize)
//            val receivedPackets = mutableSetOf<Int>()
//            while (receivedPackets.size < receivedFile.totalPackets) {
//                val buffer = ByteArray(packetSize + 4)
//                val packet = DatagramPacket(buffer, buffer.size)
//                socket.receive(packet)
//                val offset = ((packet.data[0].toInt() shl 8) or (packet.data[1].toInt() and 0xFF)) * packetSize
//                val length = (packet.data[2].toInt() shl 8) or (packet.data[3].toInt() and 0xFF)
//                System.arraycopy(packet.data, 3, receiveData, offset, length)
//                receivedPackets.add(packetNumber)
//                packetNumber++
//            }
//            File(getUserProfile() + receivedFile.fileName).writeBytes(receiveData)
//            socket.close()
//            receivedPort.remove(port)
//        } catch (e: Exception) {
//            socket.close()
//            receivedPort.remove(port)
//            println(e)
//        }
//    }
}